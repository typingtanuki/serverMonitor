import {
    CSSResult,
    customElement,
    LitElement,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import progressStyle from "./progress-bar.less";
import {MonitorType} from "../rest/types";
import {progressBarTemplate} from "./progress-bar-template";

@customElement('progress-bar')
export class ProgressBar extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(progressStyle);
    }

    static get properties() {
        return {
            type: {type: String},
            label: {type: String},
            min: {type: Number},
            max: {type: Number},
            current: {type: Number},
            warn: {type: Number}
        };
    }

    public type: MonitorType;

    public min: number;
    public max: number;
    public current: number;
    public warn: number;
    public label: string = "";

    constructor() {
        super();
    }

    public render(): TemplateResult {
        return progressBarTemplate(this);
    }
}