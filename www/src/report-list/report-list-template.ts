import {html, TemplateResult} from "lit-element";
import {ReportList} from "./report-list";
import {iconForType, Report} from "../rest/types";

export function reportListTemplate(reportList: ReportList): TemplateResult {
    return html`${reportList.reports.map(report => {
        return html`${formatHeader(reportList, report)}${formatReport(report)}`;
    })}`;
}

function formatHeader(reportList: ReportList, report: Report): TemplateResult {
    if (reportList.lastType !== report.type) {
        reportList.lastType = report.type;
        return html`<div class="header"><icon-svg .icon="${iconForType(report.type)}"></icon-svg>${report.type}</div>`;
    }
    return html``;
}

function formatReport(report: Report): TemplateResult {
    return html`<report-entry .report="${report}"></report-entry>`;
}