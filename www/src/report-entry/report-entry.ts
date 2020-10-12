import {
    CSSResult,
    customElement,
    LitElement,
    property,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import reportStyle from "./report-entry.less";
import {DisplayLine} from "../display-line/display-line";
import {DisplayGauge} from "../display-gauge/display-gauge";
import {Detail, Report} from "../rest/types";
import {CollapseView} from "../collapse-view/collapse-view";
import {reportEntryTemplate} from "./report-entry-template";

@customElement('report-entry')
export class ReportEntry extends LitElement {
    @property()
    public report: Report;

    public readonly visited: string[] = [];

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
        return reportEntryTemplate(this);
    }

    public keys(): string[] {
        const details: { [id: string]: Detail } = this.report.details;
        return Object.keys(details);
    }
}