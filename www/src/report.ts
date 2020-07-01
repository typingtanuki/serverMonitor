export interface HistoryDetail extends Detail {
    values: string[];
    dates: string[];
    max: string;
    limit: string;

}

export interface Detail {
    type?: string;

    [key: string]: any;
}

export interface Report {
    title: string;
    details: { [id: string]: Detail };
}