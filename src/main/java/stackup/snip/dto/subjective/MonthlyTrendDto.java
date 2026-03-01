package stackup.snip.dto.subjective;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MonthlyTrendDto {

    private final List<String> months = new ArrayList<>();
    private final List<Integer> counts = new ArrayList<>();

    public void addMonth(String month) {
        months.add(month);
    }

    public void addCount(Integer count) {
        counts.add(count);
    }

    public boolean ifContainsMonth(String month) {
        return months.contains(month);
    }

    public int getCountFromMonth(String month) {
        int index = months.indexOf(month);
        return counts.get(index);
    }
}

