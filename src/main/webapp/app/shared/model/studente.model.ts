import { Moment } from 'moment';
import { ICorso } from 'app/shared/model/corso.model';

export interface IStudente {
    id?: number;
    data_nascita?: Moment;
    nome?: string;
    cognome?: string;
    matricola?: string;
    facoltaNome?: string;
    facoltaId?: number;
    corsi?: ICorso[];
}

export class Studente implements IStudente {
    constructor(
        public id?: number,
        public data_nascita?: Moment,
        public nome?: string,
        public cognome?: string,
        public matricola?: string,
        public facoltaNome?: string,
        public facoltaId?: number,
        public corsi?: ICorso[]
    ) {}
}
