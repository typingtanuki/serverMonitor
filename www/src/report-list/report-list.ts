import {CSSResult, customElement, html, LitElement, property, TemplateResult, unsafeCSS} from 'lit-element';
import reportStyle from "./report-list.less";
import {ReportEntry} from "../report-entry/report-entry";
import {MonitorType, Report} from "../rest/types";
import {iconForType} from "../icon-svg/icon-svg";

@customElement('report-list')
export class ReportList extends LitElement {
    @property()
    public reports: Report[];
    private lastType: MonitorType | null = null;


    constructor() {
        super();
    }

    public static dependencies(): any[] {
        return [ReportEntry];
    }

    public static get styles(): CSSResult {
        return unsafeCSS(reportStyle);
    }

    public render(): TemplateResult {
        return html`${this.reports.map(report => {
            return html`${this.formatHeader(report)}${this.formatReport(report)}`;
        })}`;
    }

    private formatHeader(report: Report): TemplateResult {
        if (this.lastType !== report.type) {
            this.lastType = report.type;
            return html`<div class="header"><icon-svg icon="${iconForType(report.type)}"></icon-svg>${report.type}</div>`;
        }
        return html``;
    }

    private formatReport(report: Report): TemplateResult {
        return html`<report-entry .report="${report}"></report-entry>`;
    }

    public refresh(): void {
        const entries: NodeListOf<ReportEntry> = this.shadowRoot.querySelectorAll(
            "report-entry");
        for (let i = 0; i < entries.length; i++) {
            entries[i].requestUpdate();
        }
    }
}