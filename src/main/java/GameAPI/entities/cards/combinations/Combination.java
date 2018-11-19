package GameAPI.entities.cards.combinations;

import java.util.Objects;

public abstract class Combination {

  private final Integer value;

  Combination(Integer value) {
    this.value = value;
  }

  abstract Integer comparesWithSame(Combination combination);

  public Integer compares(Combination combination) {
    if (value > combination.value) {
      return 1;
    } else if (value < combination.value) {
      return -1;
    } else if (combination.getClass() == getClass()) {
      return comparesWithSame(combination);
    } else {
      throw new RuntimeException("Different combination with same value");
    }

  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ", value : " + value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o.getClass().getSuperclass() == getClass()) return false;
    return compares((Combination) o) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
