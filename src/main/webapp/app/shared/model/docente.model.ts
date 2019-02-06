export interface IDocente {
    id?: number;
    nome?: string;
    cognome?: string;
    facoltaNome?: string;
    facoltaId?: number;
}

export class Docente implements IDocente {
    constructor(
        public id?: number,
        public nome?: string,
        public cognome?: string,
        public facoltaNome?: string,
        public facoltaId?: number
    ) {}
}
