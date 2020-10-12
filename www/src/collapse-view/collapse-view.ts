import {
    CSSResult,
    customElement,
    LitElement,
    property,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import progressStyle from "./collapse-view.less";
import {icon, Icon, iconCollapse} from "../icon-svg/icons";
import Velocity from "velocity-animate";
import {collapseViewTemplate} from "./collapse-view-template";

@customElement('collapse-view')
export class CollapseView extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(progressStyle);
    }

    @property({type: String})
    public title: string;
    @property({type: Boolean})
    public collapsed: boolean;
    @property({type: Object})
    public iconCollapse: Icon = icon(iconCollapse);

    constructor() {
        super();

        this.title = "";
        this.collapsed = true;
    }

    public render(): TemplateResult {
        return collapseViewTemplate(this);
    }

    public attributeChangedCallback(name: string, oldVal: any, newVal: any): void {
        super.attributeChangedCallback(name, oldVal, newVal);
    }

    public toggle(): void {
        this.collapsed = !this.collapsed;
        const body: HTMLDivElement | null = this.shadowRoot.querySelector(".body");

        if (body == null) {
            return;
        }

        if (this.collapsed) {
            Velocity(body, {
                height: 0
            }, {
                duration: 200,
                easing: "swing"
            });
        } else {
            Velocity(body, {
                height: body.scrollHeight
            }, {
                duration: 200,
                easing: "swing"
            });
        }
        this.requestUpdate();
    }
}