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

    const hidden = [];

    for (let i = 0; i < keys.length; i++) {
        const key = keys[i];
        const entry = details[keys[i]];
        if (isHistory(entry)) {
            const sub = document.createElement("li");
            element.appendChild(sub);
            const values = entry["values"];
            const dates = entry["dates"];
            const max = parseInt(entry["max"]);
            const limit = parseInt(entry["limit"]);
            sub.appendChild(makeLine(values, dates, max, limit));
            hidden.push(key);
        } else if (key === "Do Update") {
            hidden.push(key);
        } else if (String(entry) === "[object Object]") {
            element.appendChild(makeBullet(keys[i], ""));
            const sub = document.createElement("li");
            const subsub = document.createElement("ul");
            element.appendChild(sub);
            sub.appendChild(subsub);
            makeList(entry, hidden, subsub);
            hidden.push(key);
        }
    }

    if (keys.indexOf("Current Usage") !== -1 && keys.indexOf("Maximum Usage") !== -1) {
        const sub = document.createElement("li");
        element.appendChild(sub);
        const current = parseInt(details["Current Usage"]);
        const max = parseInt(details["Maximum Usage"]);
        sub.appendChild(makeGauge(current, max));
    }

    makeList(details, hidden, element);
    return element;
}

function makeList(details, hidden, element) {
    const keys = Object.keys(details);
    for (let i = 0; i < keys.length; i++) {
        if (hidden.indexOf(keys[i]) > -1) {
            continue;
        }
        element.appendChild(makeBullet(keys[i], details[keys[i]]));
    }
}

function makeBullet(left, right) {
    const sub = document.createElement("li");
    const detail = document.createElement("span");
    const key = document.createElement("span");
    key.classList.add("key");
    key.appendChild(document.createTextNode(left + ": "));
    detail.appendChild(key);
    const value = document.createElement("span");
    value.classList.add("value");
    value.appendChild(document.createTextNode(right));
    detail.appendChild(value);
    sub.appendChild(detail);

    return sub;
}

function isHistory(value) {
    return value.hasOwnProperty("type") && value["type"] === "history";
}
