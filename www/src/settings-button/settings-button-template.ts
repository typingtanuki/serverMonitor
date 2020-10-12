import {html, TemplateResult} from "lit-element";
import {SettingsButton} from "./settings-button";

export function settingsButtonTemplate(settingsButton: SettingsButton): TemplateResult {
    return html`<icon-svg .icon="${settingsButton.iconSettings}">
        </icon-svg>${settingsButton.label}`;
}