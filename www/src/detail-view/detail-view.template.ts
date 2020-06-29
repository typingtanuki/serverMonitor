import {DetailView} from "./detail-view";
import {html, TemplateResult} from "lit-element";

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
    return html`<table class="settings">
    <tr><th>Name</th><td><input type="text" value="${element.settings.identity}"/></td></tr>
    <tr><th>Monitor time</th><td><input type="text" value="${element.settings.monitorTime}"/></td></tr>
    <tr><th>Debounce time</th><td><input type="text" value="${element.settings.debounceTime}"/></td></tr>
    <tr><th>Port</th><td><input type="text" value="${element.settings.port}"/></td></tr>
    <tr><th>Teams hook</th><td><input type="text" value="${element.settings.teamsHook}"/></td></tr>
    <tr><th colspan="2">CPU</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.cpu.enabled}"/></td></tr>
        <tr><th>Warn level</th><td><input type="text" value="${element.settings.cpu.maxUsage}"/></td></tr>
        <tr><th>History Size</th><td><input type="text" value="${element.settings.cpu.historySize}"/></td></tr>
    <tr><th colspan="2">Memory</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.memory.enabled}"/></td></tr>
        <tr><th>Warn level</th><td><input type="text" value="${element.settings.memory.maxUsage}"/></td></tr>
        <tr><th>History Size</th><td><input type="text" value="${element.settings.memory.historySize}"/></td></tr>
    <tr><th colspan="2">Network</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.network.enabled}"/></td></tr>
        <tr><th>History Size</th><td><input type="text" value="${element.settings.network.historySize}"/></td></tr>
    <tr><th colspan="2">Disks</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.disk.enabled}"/></td></tr>
        <tr><th>Warn level</th><td><input type="text" value="${element.settings.disk.maxUsage}"/></td></tr>
        <tr><th>Mounts</th><td><input type="text" value="${element.settings.disk.mounts}"/></td></tr>
    <tr><th colspan="2">Process</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.process.enabled}"/></td></tr>
        <tr><th>Processes</th><td><input type="text" value="${element.settings.process.monitoring}"/></td></tr>
    <tr><th colspan="2">Ping</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.ping.enabled}"/></td></tr>
        <tr><th>Servers</th><td><input type="text" value="${element.settings.ping.monitoring}"/></td></tr>
    <tr><th colspan="2">Handshake</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.handshake.enabled}"/></td></tr>
        <tr><th>Servers</th><td><input type="text" value="${element.settings.handshake.monitoring}"/></td></tr>
        <tr><th>Timeout</th><td><input type="text" value="${element.settings.handshake.maxHandshakeTime}"/></td></tr>
    <tr><th colspan="2">Update</th></tr>
        <tr><th>Enabled</th><td><input type="checkbox" .checked="${element.settings.updates.enabled}"/></td></tr>
</table>`;
}