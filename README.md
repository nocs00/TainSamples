## Instructions

The purpose of this test is to see how you write and read Java code. 
Test assignment consists of 3 tasks 1 of which is done during live coding session.

After you do remaining 2 tasks -
 deliver an updated javatest-<yourname>.zip, 
 which consists of the original javatest.zip plus all your modifications and additions. 

When you are done, **please also write down how long time you spent on each question**, 
if there were any unclear points and what assumptions you made in those cases.

Feel free to ask any questions to your tech contact at Tain.

Also you are free to deliver your results via public VCS(GitHub, BitBucket etc). 

### 1: Currency Conversion API

#### Summary:

You should implement application that provides currency rates via API.

#### What to do:
This application should match the following requirements:
* Currency rates should be loaded from file (find it in the rates_response_sample.json file). File contains only 3 rates: EUR->GBP, EUR->USD, EUR->UAH
* Calculate missing cross-currency rates based on data from file:
* We should calculate all other missing rates: GBP->USD, GBP->UAH, UAH->USD
* When we have all possible currency pairs, we should calculate reverse rates: GBP->EUR, USD->EUR, UAH->EUR, USD->GBP, UAH->GBP, USD->UAH
* Implement Currency API based on REST Web Service that provides:
* All possible currency rates
* Currency rate by source currency and target currency
* The result of output should be in JSON format. There are no other specific requirements on data structure or API specifications.

### 2: PokerHands

#### Summary:

Implement a program which maps a stream of dealer cards in 'Texas Hold'em' -
 like poker to a list of winning hands.

#### What to do:

You have a poker table with next rules: each player is dealt n cards(2 by default) at the beginning(such cards are called 'hole cards').
Then dealer starts drawing cards from the deck of N cards (52 by default) one by one face up. You should write a method which effectively 
maps each drawn card to a list of winning hands. 
 
Differences from classic Texas Hold'em:
1. In this version - dealer draws cards one by one. There are no pre-flop
2. Dealer continues to draw cards after there are 5 cards on a table.
3. Players always stand
4. Once winners are identified - game continues(dealer draws another card) **until dealer draws all cards from a deck**. Players are not kicked out from a table

Winning combinations are standard. You can refer this article for reference:
[Poker winning combinations](https://www.pokerstars.com/poker/games/rules/hand-rankings/)
 
Also make sure to cover cases with tie/kickers:
[Kickers And Ties](https://en.wikipedia.org/wiki/Texas_hold_%27em#Kickers_and_ties)
 
Dealer input stream should take input cards as a stream of strings. 
Each card is a 2 character string where 1st character is a card face and 2nd character is a card suit.

Card faces:
2,3,4,5,6,7,8,9,T,J,Q,K,A

Card suits:
H,S,C,D


Here are some examples of draws with outputs:

```

 Players: John[holeCards=2C 5D], Pete[holeCards=AC TS]
 Turn 1: Dealer picks 4S; Winner:Pete(High card)
 Turn 2: Dealer picks 2D; Winner:John(Pair)
 Turn 3: Dealer picks AH; Winner:Pete(Pair)
 Turn 4: Dealer picks 3D; Winner:John(Straight)
 Turn 5: Dealer picks QD; Winner:John(Straight)
 Turn 6: Dealer picks KS; Winner:John(Straight)
 Turn 7: Dealer picks JD; Winner:Pete(Straight)
 .........

```