import {CSSResult, customElement, html, LitElement, property, TemplateResult, unsafeCSS} from 'lit-element';
import reportStyle from "./report-entry.less";
import {DisplayLine} from "../display-line/display-line";
import {DisplayGauge} from "../display-gauge/display-gauge";
import {Detail, HistoryDetail, Report} from "../rest/types";

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
        return [DisplayLine, DisplayGauge];
    }

    public static get styles(): CSSResult {
        return unsafeCSS(reportStyle);
    }

    public render(): TemplateResult {
        return html`<div class="report-details">
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

    private handleGauge(): TemplateResult {
        if (isGauge(this.report.details)) {
            return ReportEntry.formatGauge(this.report.details);
        }
        return html``;
    }

    private formatKey(key: string): TemplateResult {
        const details: Detail = this.report.details[key];
        return html`${this.doFormat(details, key)}`;
    }

    private doFormat(details: Detail, key: string): TemplateResult {
        this.visited.push(key);

        if (isHistory(details)) {
            return ReportEntry.formatHistory(<HistoryDetail>details);
        }
        if (isHidden(key)) {
            return html``;
        }
        if (isString(details) || isNumber(details)) {
            return ReportEntry.formatString(key, String(details));
        }
        if (isObject(details)) {
            return ReportEntry.formatObject(details);
        }
    }

    private static formatHistory(details: HistoryDetail): TemplateResult {
        return html`<display-line 
                               .values="${details.values}" 
                               .dates="${details.dates}"
                               .max="${parseInt(details.max)}"
                               .limit="${parseInt(details.limit)}"></display-line>`;
    }

    private static formatString(key: string, value: string): TemplateResult {
        return html`<div><span class="key">${key}: </span><span class="value">${String(value)}</span></div>`;
    }

    private static formatObject(details: Detail): TemplateResult {
        const keys: string[] = Object.keys(details);
        return html`${keys.map(key => ReportEntry.subList(key, details))}`;
    }

    private static subList(key: string, details: Detail): TemplateResult {
        return ReportEntry.formatString(key, details[key]);
    }

    private static formatGauge(details: Detail): TemplateResult {
        return html`<display-gauge 
                         .current="${parseInt((<any>details)["Current Usage"])}"
                         .max="${parseInt((<any>details)["Maximum Usage"])}"></display-gauge>`;
    }
}


function isGauge(value: Detail): boolean {
    const keys: string[] = Object.keys(value);
    return keys.indexOf("Current Usage") !== -1 && keys.indexOf("Maximum Usage") !== -1;
}

function isHistory(value: Detail): boolean {
    return value.hasOwnProperty("type") && value.type === "history";
}

function isHidden(key: string): boolean {
    return key === "Do Update";
}

function isObject(details: Detail): boolean {
    return String(details) === "[object Object]";
}

function isString(details: Detail): boolean {
    return typeof details === 'string' || details instanceof String;
}

function isNumber(details: Detail): boolean {
    return typeof details === 'number' || details instanceof Number;
}