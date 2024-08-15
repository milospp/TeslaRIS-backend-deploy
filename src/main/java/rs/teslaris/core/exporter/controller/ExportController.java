package rs.teslaris.core.exporter.controller;

import java.util.Date;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.teslaris.core.exporter.service.interfaces.OutboundExportService;
import rs.teslaris.core.exporter.util.OAIErrorFactory;
import rs.teslaris.core.importer.model.oaipmh.common.OAIPMHResponse;
import rs.teslaris.core.importer.model.oaipmh.common.Request;
import rs.teslaris.core.importer.utility.OAIPMHParseUtility;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ExportController {

    private final OutboundExportService outboundExportService;

    @Value("export.base.url")
    private String baseUrl;

    @GetMapping(value = "/OAIHandlerOpenAIRECRIS", produces = "application/xml")
    public OAIPMHResponse handleOAIOpenAIRECRIS(@RequestParam String verb,
                                                @RequestParam(required = false)
                                                String metadataPrefix,
                                                @RequestParam(required = false) String from,
                                                @RequestParam(required = false) String until,
                                                @RequestParam(required = false) String set,
                                                @RequestParam(required = false) String identifier,
                                                @RequestParam(required = false)
                                                String resumptionToken) {
        return performExport("OAIHandlerOpenAIRECRIS", verb, metadataPrefix, from, until, set,
            identifier, resumptionToken);
    }

    private OAIPMHResponse performExport(String handlerName,
                                         String verb,
                                         String metadataPrefix,
                                         String from,
                                         String until,
                                         String set,
                                         String identifier,
                                         String resumptionToken) {
        var response = new OAIPMHResponse();
        response.setResponseDate(new Date());
        response.setRequest(new Request(verb, set, metadataPrefix, baseUrl + "/" + handlerName));

        switch (verb) {
            case "Identify":
                response.setIdentify(outboundExportService.identifyHandler(handlerName));
                break;
            case "ListSets":
                response.setListSets(outboundExportService.listSetsForHandler(handlerName));
                break;
            case "ListMetadataFormats":
                response.setListMetadataFormats(
                    outboundExportService.listMetadataFormatsForHandler(handlerName));
                break;
            case "ListRecords":
                if (Objects.nonNull(resumptionToken)) {
                    if (!OAIPMHParseUtility.validateResumptionToken(resumptionToken)) {
                        response.setError(OAIErrorFactory.constructBadResumptionTokenError());
                        break;
                    }

                    OAIPMHParseUtility.ResumptionTokenData dataFromToken;
                    try {
                        dataFromToken = OAIPMHParseUtility.parseResumptionToken(resumptionToken);
                    } catch (IllegalArgumentException e) {
                        response.setError(OAIErrorFactory.constructBadResumptionTokenError());
                        break;
                    }

                    response.setListRecords(
                        outboundExportService.listRequestedRecords(handlerName,
                            dataFromToken.format(),
                            dataFromToken.from(), dataFromToken.until(), dataFromToken.set(),
                            response, dataFromToken.page()));
                } else {
                    response.setListRecords(
                        outboundExportService.listRequestedRecords(handlerName, metadataPrefix,
                            from, until, set, response, 0));
                }
                break;
            case "GetRecord":
                response.setGetRecord(
                    outboundExportService.listRequestedRecord(handlerName, metadataPrefix,
                        identifier, response));
                break;
            default:
                response.setError(OAIErrorFactory.constructBadVerbError());
        }

        return response;
    }
}
