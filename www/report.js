function formatReports(reports, node) {
    while (node.hasChildNodes()) {
        node.removeChild(node.firstChild);
    }

    for (let i = 0; i < reports.length; i++) {
        const element = document.createElement("div");
        element.classList.add("monitor");
        element.appendChild(formatReportTitle(reports[i]));
        element.appendChild(formatReportDetails(reports[i]));
        node.appendChild(element);
    }
}

function formatReportTitle(report) {
    const element = document.createElement("div");
    element.classList.add("report-title");
    element.appendChild(document.createTextNode(report.title));
    return element;
}

function formatReportDetails(report) {
    const element = document.createElement("ul");
    element.classList.add("report-details");
    const details = report.details;
    const keys = Object.keys(details);

    if (keys.indexOf("Current Usage") !== -1 && keys.indexOf("Maximum Usage") !== -1) {
        const sub = document.createElement("li");
        element.appendChild(sub);
        const current = parseInt(details["Current Usage"]);
        const max = parseInt(details["Maximum Usage"]);
        sub.appendChild(makeGauge(current, max));
    }

    for (let i = 0; i < keys.length; i++) {
        const sub = document.createElement("li");
        element.appendChild(sub);

        const detail = document.createElement("span");
        const key = document.createElement("span");
        key.classList.add("key");
        key.appendChild(document.createTextNode(keys[i] + ": "));
        detail.appendChild(key);
        const value = document.createElement("span");
        value.classList.add("value");
        value.appendChild(document.createTextNode(details[keys[i]]));
        detail.appendChild(value);
        sub.appendChild(detail);
    }
    return element;
}