import { Profile } from '../profile/profile.model';
import { Recommendation } from '../recommendation/recommendation.model';
import { Exchange } from '../screening/exchange.model';

export class ConversationSummary {
  userProfile: Profile;
  exchanges: Exchange[];
  recommendation: Recommendation;
}
