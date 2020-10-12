import {html, TemplateResult} from "lit-element";
import {ReportEntry} from "./report-entry";
import {
    isCause,
    isGauge,
    isHidden,
    isHistory,
    isNumber,
    isObject,
    isString
} from "../utils";
import {Detail, HistoryDetail, ReportConstants} from "../rest/types";

export function reportEntryTemplate(reportEntry: ReportEntry): TemplateResult {
    return html`<div class="report-details">
            ${handleTitle(reportEntry)}
            ${handleGauge(reportEntry)}
            ${reportEntry.keys().map(key => formatKey(reportEntry, key))}
            ${handleOthers(reportEntry)}
            </div>`;
}

function handleOthers(reportEntry: ReportEntry): TemplateResult {
    const keys: string[] = reportEntry.keys();
    const left: string[] = keys.filter(key => !reportEntry.visited.includes(key));
    return html`${left.map(key => formatKey(reportEntry, key))}`;
}

function handleTitle(reportEntry: ReportEntry): TemplateResult {
    return html`<div class="title">${reportEntry.report.title}</div>`;
}

function handleGauge(reportEntry: ReportEntry): TemplateResult {
    if (isGauge(reportEntry.report.details)) {
        return formatGauge(reportEntry.report.details);
    }
    return html``;
}

function formatGauge(details: Detail): TemplateResult {
    return html`<display-gauge 
                         .current="${parseInt((<any>details)[ReportConstants.USAGE_CURRENT])}"
                         .max="${parseInt((<any>details)[ReportConstants.USAGE_MAX])}"></display-gauge>`;
}

function formatKey(reportEntry: ReportEntry, key: string): TemplateResult {
    const details: Detail = reportEntry.report.details[key];
    if (details === null) {
        return html``;
    }
    return html`${doFormatEntry(reportEntry, details, key)}`;
}

function doFormatEntry(reportEntry: ReportEntry,
                       details: Detail,
                       key: string): TemplateResult {
    reportEntry.visited.push(key);

    if (isHistory(details)) {
        return formatHistory(key, <HistoryDetail>details);
    }
    if (isHidden(key)) {
        return html``;
    }
    if (isCause(key)) {
        return html`<collapse-view .title="${key}"><div class="code">${String(details)}</div></collapse-view>`;
    }
    if (isString(details) || isNumber(details)) {
        return formatString(key, String(details));
    }
    if (isObject(details)) {
        return formatObject(key, details);
    }
}

function formatHistory(key: string, details: HistoryDetail): TemplateResult {
    return html`<div class="legend">${key}</div>
                    <display-line 
                               .values="${details.values}" 
                               .dates="${details.dates}"
                               .max="${parseInt(details.max)}"
                               .limit="${parseInt(details.limit)}"></display-line>`;
}

function formatString(key: string, value: string): TemplateResult {
    return html`<div><span class="key">${key}: </span><span class="value">${String(
        value)}</span></div>`;
}

function formatObject(key: string, details: Detail): TemplateResult {
    const keys: string[] = Object.keys(details);
    return html`
<collapse-view .title="${key}"><div>
    ${keys.length === 0 ? "None" : ""}
    ${keys.map(key => subList(key, details))}
</div></collapse-view>`;
}

function subList(key: string, details: Detail): TemplateResult {
    return formatString(key, details[key]);
}