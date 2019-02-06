export interface IFacolta {
    id?: number;
    nome?: string;
}

export class Facolta implements IFacolta {
    constructor(public id?: number, public nome?: string) {}
}
