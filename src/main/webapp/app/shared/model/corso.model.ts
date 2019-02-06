import { IStudente } from 'app/shared/model/studente.model';

export interface ICorso {
    id?: number;
    nome?: string;
    facoltaNome?: string;
    facoltaId?: number;
    docenteCognome?: string;
    docenteId?: number;
    studenti?: IStudente[];
}

export class Corso implements ICorso {
    constructor(
        public id?: number,
        public nome?: string,
        public facoltaNome?: string,
        public facoltaId?: number,
        public docenteCognome?: string,
        public docenteId?: number,
        public studenti?: IStudente[]
    ) {}
}
