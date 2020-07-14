import {
    CSSResult,
    customElement,
    html,
    LitElement,
    property,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import reportStyle from "./report-entry.less";
import {DisplayLine} from "../display-line/display-line";
import {DisplayGauge} from "../display-gauge/display-gauge";
import {Detail, HistoryDetail, Report, ReportConstants} from "../rest/types";
import {
    isCause,
    isGauge,
    isHidden,
    isHistory,
    isNumber,
    isObject,
    isString
} from "../utils";
import {CollapseView} from "../collapse-view/collapse-view";

@customElement('report-entry')
export class ReportEntry extends LitElement {
    @property()
    public report: Report;

    private readonly visited: string[] = [];

    constructor(report: Report) {
        super();

        this.report = report;
        this.visited.length = 0;
    }

    public static dependencies(): any[] {
        return [DisplayLine, DisplayGauge, CollapseView];
    }

    public static get styles(): CSSResult {
        return unsafeCSS(reportStyle);
    }

    public render(): TemplateResult {
        return html`<div class="report-details">
            ${this.handleTitle()}
            ${this.handleGauge()}
            ${this.keys().map(key => this.formatKey(key))}
            ${this.handleOthers()}
            </div>`;
    }

    private keys(): string[] {
        const details: { [id: string]: Detail } = this.report.details;
        return Object.keys(details);
    }

    private handleOthers(): TemplateResult {
        const keys: string[] = this.keys();
        const left: string[] = keys.filter(key => !this.visited.includes(key));
        return html`${left.map(key => this.formatKey(key))}`;
    }

    private handleTitle(): TemplateResult {
        return html`<div class="title">${this.report.title}</div>`;
    }

    private handleGauge(): TemplateResult {
        if (isGauge(this.report.details)) {
            return ReportEntry.formatGauge(this.report.details);
        }
        return html``;
    }

    private formatKey(key: string): TemplateResult {
        const details: Detail = this.report.details[key];
        if (details === null) {
            return html``;
        }
        return html`${this.doFormat(details, key)}`;
    }

    private doFormat(details: Detail, key: string): TemplateResult {
        this.visited.push(key);

        if (isHistory(details)) {
            return ReportEntry.formatHistory(key, <HistoryDetail>details);
        }
        if (isHidden(key)) {
            return html``;
        }
        if (isCause(key)) {
            return html`<collapse-view .title="${key}"><div class="code">${String(details)}</div></collapse-view>`;
        }
        if (isString(details) || isNumber(details)) {
            return ReportEntry.formatString(key, String(details));
        }
        if (isObject(details)) {
            return ReportEntry.formatObject(key, details);
        }
    }

    private static formatHistory(key: string, details: HistoryDetail): TemplateResult {
        return html`<div class="legend">${key}</div>
                    <display-line 
                               .values="${details.values}" 
                               .dates="${details.dates}"
                               .max="${parseInt(details.max)}"
                               .limit="${parseInt(details.limit)}"></display-line>`;
    }

    private static formatString(key: string, value: string): TemplateResult {
        return html`<div><span class="key">${key}: </span><span class="value">${String(
            value)}</span></div>`;
    }

    private static formatObject(key: string, details: Detail): TemplateResult {
        const keys: string[] = Object.keys(details);
        return html`
<collapse-view .title="${key}"><div>
    ${keys.length === 0 ? "None" : ""}
    ${keys.map(key => ReportEntry.subList(key, details))}
</div></collapse-view>`;
    }

    private static subList(key: string, details: Detail): TemplateResult {
        return ReportEntry.formatString(key, details[key]);
    }

    private static formatGauge(details: Detail): TemplateResult {
        return html`<display-gauge 
                         .current="${parseInt((<any>details)[ReportConstants.USAGE_CURRENT])}"
                         .max="${parseInt((<any>details)[ReportConstants.USAGE_MAX])}"></display-gauge>`;
    }
}