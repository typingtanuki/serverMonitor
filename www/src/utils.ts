import {Detail, ReportConstants} from "./rest/types";

export function isGauge(value: Detail): boolean {
    const keys: string[] = Object.keys(value);
    return keys.indexOf(ReportConstants.USAGE_CURRENT) !==
        -1 &&
        keys.indexOf(ReportConstants.USAGE_MAX) !==
        -1;
}

export function isHistory(value: Detail): boolean {
    return value.hasOwnProperty("type") && value.type === "history";
}

export function isCause(key: string): boolean {
    return key === ReportConstants.CAUSE;
}

export function isHidden(key: string): boolean {
    switch (key) {
        case ReportConstants.ACTION_UPDATE:
        case ReportConstants.USAGE_MAX:
        case ReportConstants.USAGE_CURRENT:
            return true;

    }
    return false;
}

export function isObject(details: Detail): boolean {
    return String(details) === "[object Object]";
}

export function isString(details: Detail): boolean {
    return typeof details === 'string' || details instanceof String;
}

export function isNumber(details: Detail): boolean {
    return typeof details === 'number' || details instanceof Number;
}