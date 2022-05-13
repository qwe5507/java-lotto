package lotto.domain;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.IntStream;
import lotto.domain.money.Money;
import lotto.domain.strategy.GenerateNumbersStrategy;

public class LotteryShop {

  public static final Money PRICE_PER_PLAY_FOR_LOTTO = Money.createWon(1000);

  private static final String PRICE_EXCEPTION_MESSAGE = String.format("로또 1장의 가격은 %s 입니다",
      PRICE_PER_PLAY_FOR_LOTTO.won());
  private static final int START_TICKET_COUNT = 0;

  public LottoTickets sell(Money purchaseAmount, GenerateNumbersStrategy generateNumbersStrategy) {
    checkGreaterThanMinimumPrice(purchaseAmount);

    List<LottoTicket> lottoTickets = IntStream.range(START_TICKET_COUNT,
            getAvailableLottoTicketCount(purchaseAmount))
        .mapToObj(i -> createLottoTicket(generateNumbersStrategy))
        .collect(toList());
    return new LottoTickets(lottoTickets);
  }

  private int getAvailableLottoTicketCount(Money receivedMoney) {
    return receivedMoney.divide(PRICE_PER_PLAY_FOR_LOTTO).value();
  }

  private void checkGreaterThanMinimumPrice(Money purchaseMoney) {
    if (purchaseMoney.lessThan(PRICE_PER_PLAY_FOR_LOTTO)) {
      throw new IllegalArgumentException(PRICE_EXCEPTION_MESSAGE);
    }
  }

  private LottoTicket createLottoTicket(GenerateNumbersStrategy generateNumbersStrategy) {
    return new LottoTicket(createLottoNumbers(generateNumbersStrategy));
  }

  private List<Integer> createLottoNumbers(GenerateNumbersStrategy generateNumbersStrategy) {
    return generateNumbersStrategy.generate();
  }
}
