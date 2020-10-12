import {
    CSSResult,
    customElement,
    LitElement,
    TemplateResult,
    unsafeCSS
} from "lit-element";
import buttonStyle from "./settings-button.less";
import {ServerInfo} from "../rest/types";
import {Icon, icon, iconSettings} from "../icon-svg/icons";
import {settingsButtonTemplate} from "./settings-button-template";

@customElement('settings-button')
export class SettingsButton extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(buttonStyle);
    }

    static get properties() {
        return {
            server: {type: Object},
            label: {type: String},
            iconSettings: {type: Object}
        };
    }

    public server: ServerInfo;
    public label: string;
    public iconSettings: Icon = icon(iconSettings);

    constructor() {
        super();
    }

    public render(): TemplateResult {
        return settingsButtonTemplate(this);
    }
}