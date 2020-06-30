import {DetailView} from "./detail-view";
import {html, TemplateResult} from "lit-element";
import {Settings} from "../rest/rest-client";

export function detailViewTemplate(element: DetailView): TemplateResult {
    return html`
    <div id="server">${element.server.name}</div>
    <div id="response">
        <settings-button @click="{${element.showSettings}"></settings-button>
        <report-list class="failure" .reports="${element.failure}"></report-list>
        <report-list class="success" .reports="${element.success}"></report-list>
    </div>`;
}

export function settingsViewTemplate(element: DetailView): TemplateResult {
    return html`
    <div id="server">${element.server.name}</div>
    <div id="response"><table class="settings">
        ${formSection("Global")}
        ${formText("Name", element.settings, "identity")}
        ${formText("Monitor time", element.settings, "monitorTime")}
        ${formText("Debounce time", element.settings, "debounceTime")}
        ${formText("API port", element.settings, "port")}
        ${formText("Teams Hook", element.settings, "teamsHook")}
        
        ${formSection("CPU")}
        ${formCheckbox("Enabled", element.settings, "cpu.enabled")}
        ${formText("Warn Level", element.settings, "cpu.maxUsage")}
        ${formText("History Size", element.settings, "cpu.historySize")}
    
        ${formSection("Memory")}
        ${formCheckbox("Enabled", element.settings, "memory.enabled")}
        ${formText("Warn Level", element.settings, "memory.maxUsage")}
        ${formText("History Size", element.settings, "memory.historySize")}
    
        ${formSection("Network")}
        ${formCheckbox("Enabled", element.settings, "network.enabled")}
        ${formText("History Size", element.settings, "network.historySize")}
    
        ${formSection("Disks")}
        ${formCheckbox("Enabled", element.settings, "disk.enabled")}
        ${formText("Warn Level", element.settings, "disk.maxUsage")}
        ${formText("Disks", element.settings, "disk.mounts")}
    
        ${formSection("Process")}
        ${formCheckbox("Enabled", element.settings, "process.enabled")}
        ${formText("Processes", element.settings, "process.monitoring")}
    
        ${formSection("Ping")}
        ${formCheckbox("Enabled", element.settings, "ping.enabled")}
        ${formText("Servers", element.settings, "ping.monitoring")}
    
        ${formSection("Handshake")}
        ${formCheckbox("Enabled", element.settings, "handshake.enabled")}
        ${formText("Servers", element.settings, "handshake.monitoring")}
        ${formText("Timeout", element.settings, "handshake.maxHandshakeTime")}
    
        ${formSection("System Updates")}
        ${formCheckbox("Enabled", element.settings, "updates.enabled")}
    </table></div>`;
}

function formText(name: string, settings: Settings, key: string): TemplateResult {
    return html`
    <tr class="form text" .settings="${settings}" .key="${key}">
        <th>${name}</th><td><input type="text" value="${getKey(settings, key)}"/></td>
    </tr>`;
}

function formCheckbox(name: string, settings: Settings, key: string): TemplateResult {
    return html`
    <tr class="form check" .settings="${settings}" .key="${key}">
        <th>${name}</th><td><input type="checkbox" .checked="${getKey(settings, key)}"/></td></tr>
    </tr>`;
}

function formSection(name: string): TemplateResult {
    return html`<tr><th colspan="2">${name}</th></tr>`;
}

function getKey(settings: Settings, key: string): string | boolean {
    const path: string[] = key.split(".");
    let pos: any = settings;
    for (const piece of path) {
        pos = pos[piece];
    }

    if (pos === true || pos === false) {
        return pos;
    }

    if (Array.isArray(pos)) {
        return (<string[]>pos).join("; ");
    }

    if (pos === null || pos === undefined) {
        return "";
    }

    return String(pos);
}