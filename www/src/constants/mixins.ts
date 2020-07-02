export function isAttached(element: Node): boolean {
    if (element.parentElement) {
        return isAttached(element.parentElement);
    }
    if (element.parentNode) {
        return isAttached(element.parentNode);
    }
    if ((<ShadowRoot>element).host) {
        return isAttached((<ShadowRoot>element).host);
    }

    return String(element) === "[object HTMLDocument]";
}