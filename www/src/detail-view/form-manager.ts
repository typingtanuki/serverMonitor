import {Settings} from "../rest/rest-client";
import {html, TemplateResult} from "lit-element";

export enum DataType {
    string = "string",
    array = "array",
    number = "number",
    boolean = "boolean"
}

export function formText(name: string,
                         settings: Settings,
                         key: string,
                         type: DataType): TemplateResult {
    return html`
    <tr class="form text" .settings="${settings}" .key="${key}" .type="${type}">
        <th>${name}</th><td><input type="text" value="${getKey(settings, key, type)}"/></td>
    </tr>`;
}

export function formCheckbox(name: string, settings: Settings, key: string): TemplateResult {
    return html`
    <tr class="form check" .settings="${settings}" .key="${key}">
        <th>${name}</th><td><input type="checkbox" .checked="${getKey(settings, key, DataType.boolean)}"/></td></tr>
    </tr>`;
}

export function formSection(name: string): TemplateResult {
    return html`<tr><th colspan="2">${name}</th></tr>`;
}

function getKey(settings: Settings,
                key: string,
                type: DataType): string | boolean {
    const path: string[] = key.split(".");
    let pos: any = settings;
    for (const piece of path) {
        pos = pos[piece];
    }

    if (pos === true || pos === false) {
        return pos;
    }

    if (pos === null || pos === undefined) {
        return "";
    }

    if (type === DataType.array) {
        return (<string[]>pos).join("; ");
    }

    return String(pos);
}

function putKey(settings: Settings,
                key: string,
                value: null | string | number | boolean | Array<String>): void {
    const path: string[] = key.split(".");
    const last: string = path.splice(path.length - 1, 1)[0];

    let pos: any = settings;
    for (const piece of path) {
        pos = pos[piece];
    }

    pos[last] = value;
}

interface SettingRow extends HTMLTableRowElement {
    settings: Settings;
    key: string;
    type: DataType;
}

export function buildForm(root: HTMLElement): void {
    const entries: NodeListOf<SettingRow> = <NodeListOf<SettingRow>>root.querySelectorAll("tr");
    for (let i = 0; i < entries.length; i++) {
        const tr: SettingRow = entries[i];
        if (tr.classList.contains("text")) {
            buildFormText(tr);
        } else if (tr.classList.contains("check")) {
            buildFormCheckbox(tr);
        }
    }
}

function buildFormText(tr: SettingRow): void {
    const settings: Settings = tr.settings;
    const type: DataType = tr.type;
    const key: string = tr.key;

    const rawValue: string = tr.querySelector("input").value;
    let value: null | string | number | boolean | Array<String> = null;
    switch (type) {
        case DataType.array:
            const parts: string[] = rawValue.split(";");
            const split: string[] = [];
            for (const part of parts) {
                const trimmed: string = part.trim();
                if (trimmed.length > 0) {
                    split.push(trimmed);
                }
            }
            value = split;
            break;
        case DataType.boolean:
            value = rawValue === "true";
            break;
        case DataType.string:
            value = rawValue;
            break
        case DataType.number:
            value = parseInt(rawValue, 10);
            break;
    }

    putKey(settings, key, value);
}

function buildFormCheckbox(tr: SettingRow): void {
    const settings: Settings = tr.settings;
    const key: string = tr.key;

    putKey(settings, key, tr.querySelector("input").checked);
}