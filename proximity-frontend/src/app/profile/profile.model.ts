import { Language } from '../core/languages/language.enum';
import { Gender } from './gender.enum';

export class Profile {
  gender: Gender;
  age: number;
  canton: string;
  contactDates: Date[];
  language: Language;
}
