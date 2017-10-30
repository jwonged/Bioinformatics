package exactPatternMatching;

import java.util.HashMap;
import java.util.List;

public interface Matcher {
	public HashMap<String, List<Integer>> match(final String genome, final List<String> pattern);
}
