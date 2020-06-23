import {CSSResult, customElement, html, LitElement, property, TemplateResult, unsafeCSS} from 'lit-element';
import reportStyle from "./report-entry.css";
import {Detail, HistoryDetail, Report} from "../report";
import {DisplayLine} from "../display-line/display-line";
import {DisplayGauge} from "../display-gauge/display-gauge";

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
        return html`<ul class="report-details">
            ${this.handleGauge()}
            ${this.keys().map(key => this.formatKey(key))}
            ${this.handleOthers()}
            </ul>`;
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
        if (this.isGauge(this.report.details)) {
            return this.formatGauge(this.report.details);
        }
        return html``;
    }

    private formatKey(key: string): TemplateResult {
        const details: Detail = this.report.details[key];
        return html`${
            this.isHistory(details) ? this.formatHistory(key, <HistoryDetail>details) :
                this.isHidden(key) ? this.visited.push(key) :
                    this.isString(details) || this.isNumber(details) ? this.formatString(key, String(details)) :
                        this.isObject(details) ? this.formatObject(key, details) : "Dropped " + key}`;
    }

    private isGauge(value: Detail): boolean {
        const keys: string[] = Object.keys(value);
        return keys.indexOf("Current Usage") !== -1 && keys.indexOf("Maximum Usage") !== -1;
    }

    private isHistory(value: Detail): boolean {
        return value.hasOwnProperty("type") && value.type === "history";
    }

    private isHidden(key: string): boolean {
        return key === "Do Update";
    }

    private isObject(details: Detail): boolean {
        return String(details) === "[object Object]";
    }

    private isString(details: Detail): boolean {
        return typeof details === 'string' || details instanceof String;
    }

    private isNumber(details: Detail): boolean {
        return typeof details === 'number' || details instanceof Number;
    }

    private formatHistory(key: string, details: HistoryDetail): TemplateResult {
        this.visited.push(key);
        return html`<li><display-line 
                               .values="${details.values}" 
                               .dates="${details.dates}"
                               .max="${parseInt(details.max)}"
                               .limit="${parseInt(details.limit)}"></display-line></li>`;
    }

    private formatString(key: string, value: string): TemplateResult {
        this.visited.push(key);
        return html`<li><span><span class="key">${key}: </span><span class="value">${String(value)}</span></span></li>`;
    }

    private formatObject(key: string, details: Detail): TemplateResult {
        this.visited.push(key);
        const keys: string[] = Object.keys(details);
        return html`<li><ul>${keys.map(key => this.subList(key, details))}</ul></li>`;
    }

    private subList(key: string, details: Detail): TemplateResult {
        return this.formatString(key, details[key]);
    }

    private formatGauge(details: Detail): TemplateResult {
        return html`<display-gauge 
                         .current="${parseInt((<any>details)["Current Usage"])}"
                         .max="${parseInt((<any>details)["Maximum Usage"])}"></display-gauge>`;
    }
}