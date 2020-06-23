import {clearElement, div, text} from "./layout";
import {ReportEntry} from "./report-entry/report-entry";

export interface HistoryDetail extends Detail {
    values: string[];
    dates: string[];
    max: string;
    limit: string;

}

export interface Detail {
    type?: string;

    [key: string]: any;
}

export interface Report {
    title: string;
    details: { [id: string]: Detail };
}

export function formatReports(reports: Report[], node: HTMLElement): void {
    clearElement(node);

    for (const report of reports) {
        const element = document.createElement("div");
        element.classList.add("monitor");
        element.appendChild(formatReportTitle(report));
        element.appendChild(new ReportEntry(report));
        node.appendChild(element);
    }
}

function formatReportTitle(report: Report): HTMLDivElement {
    const element: HTMLDivElement = div(text(report.title));
    element.classList.add("report-title");
    return element;
}