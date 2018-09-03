package cn.edu.bupt.rsx.textdeal.getkeywords;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

import cn.edu.bupt.rsx.htmlparser.tools.CalculateTools;
import org.ansj.domain.Term;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.edu.bupt.rsx.htmlparser.tools.ConfigTools;

public class GetKeywords {

    private final static Logger LOGGER = LoggerFactory.getLogger(GetKeywords.class);

    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    static final float d = 0.85f;
    /**
     * 最大迭代次数
     */
    private static final int max_iter = 200;
    private static final float min_diff = 0.001f;

    private static Set<String> stopWords = new HashSet<>();

    static {

        try {
            BufferedReader inBufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(GetKeywords.class.getResource("/").getPath() +
                            ConfigTools.getProperty("stop_words", null)), Charset.forName("UTF-8")));
            String line;
            while ((line = inBufferedReader.readLine()) != null) {
                line = line.trim();
                if (!stopWords.contains(line)) {
                    stopWords.add(line);

                }
            }
            inBufferedReader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员"
                + "和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序"
                + "员、系统分析员和项目经理四大类";
    }

    /**
     * textRank算法（文本型）
     *
     * @param termList
     * @param num
     * @return
     */
    public static List<Map<String, Object>> getKeyWords(List<Term> termList, int num) {
        Map<String, Set<String>> words = new HashMap<>();
        LinkedList<String> que = new LinkedList<>();
        String w;
        for (Term term : termList) {
            w = term.getName();
            if (shouldInclude(term)) {
                if (!words.containsKey(w)) {
                    words.put(w, new HashSet<>());
                }
                que.offer(w);
                if (que.size() > 5) {
                    que.poll();
                }

                for (String w1 : que) {
                    for (String w2 : que) {
                        if (w1.equals(w2)) {
                            continue;
                        }

                        words.get(w1).add(w2);
                        words.get(w2).add(w1);
                    }
                }
            }
        }
        Map<String, Float> score = new HashMap<>();
        for (int i = 0; i < max_iter; ++i) {
            Map<String, Float> m = new HashMap<>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String other : value) {
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) break;
        }
        return getResult(score, num);
    }

    /**
     * 多特征算法（主题型）
     *
     * @param header
     * @param termList
     * @param num
     * @return
     */
    public static List<Map<String, Object>> getKeyWords1(String header, List<Term> termList, int num) {
        Map<String, Integer> originData = new HashMap<>();
        Map<String, Float> localData = new HashMap<>();
        Map<String, Float> secondData = new HashMap<>();
        Map<String, Float> nominalData = new HashMap<>();
        Map<String, Float> resultData = new HashMap<>();
        int headerCount = 0;
        int status = 1;
        float result;
        for (int i = 0; i < termList.size(); i++) {
            Term t = termList.get(i);
            if (originData.get(t.getName()) == null) {
                originData.put(t.getName(), 1);
            } else {
                originData.put(t.getName(), originData.get(t.getName()) + 1);
            }
            if (localData.get(t.getName()) == null) {
                if (header.contains(t.getName()) && status == 1) {
                    localData.put(t.getName(), 1f);
                    headerCount++;
                } else {
                    status = 0;
                    localData.put(t.getName(), getPower(i - headerCount, termList.size() - headerCount));
                }
            }
            if (nominalData.get(t.getName()) == null) {
                if (!StringUtils.isBlank(t.getNatureStr())) {
                    if (t.getNatureStr().startsWith("n") || t.getNatureStr().startsWith("v")) {
                        nominalData.put(t.getName(), 1f);
                    } else if (t.getNatureStr().startsWith("t") || t.getNatureStr().startsWith("s") || t.getNatureStr().startsWith("f")) {
                        nominalData.put(t.getName(), 0.5f);
                    } else if (t.getNatureStr().startsWith("a") || t.getNatureStr().startsWith("d")) {
                        nominalData.put(t.getName(), 0.2f);
                    } else {
                        nominalData.put(t.getName(), 0f);
                    }
                } else {
                    nominalData.put(t.getName(), 0f);
                    ;
                }
            }

        }

        List<Map.Entry<String, Integer>> list = CalculateTools.sortInteger(originData);
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, Integer> map = list.get(i);

            secondData.put(map.getKey(), getPower(list.get(i).getValue(), list.get(list.size() - 1).getValue()));
        }
        for (String str : secondData.keySet()) {
            result = secondData.get(str) * 2f + nominalData.get(str) * 2f + localData.get(str) * 1f;
            resultData.put(str, result);
        }
        return getResult(resultData, num);
    }

    private static Float getPower(Integer n1, Integer c) {
        float result = n1.floatValue() / (n1.floatValue() + c.floatValue());
        return result;
    }

    private static ArrayList<Map<String, Object>> getResult(Map<String, Float> originMap, Integer num) {
        List<Map.Entry<String, Float>> resultList = CalculateTools.sortFloat(originMap);
        ArrayList<Map<String, Object>> keywords = new ArrayList<>();
        for (int i = resultList.size() - 1; i >= (num <= resultList.size() ? resultList.size() - num : 0); i--) {
            Map<String, Object> map = new HashMap<>();
            map.put("keyword", resultList.get(i).getKey());
            map.put("weight", resultList.get(i).getValue());
            keywords.add(map);
        }
        return keywords;
    }

    /**
     * 词频数目统计（主题型，视频型网址使用）
     *
     * @param termList
     * @param num
     * @return
     */
    public static List<Map<String, Object>> getKeyWords2(List<Term> termList, int num) {
        Map<String, Integer> originData = new HashMap<>();
        ArrayList<Map<String, Object>> keywords = new ArrayList<>();
        for (int i = 0; i < termList.size(); i++) {
            Term t = termList.get(i);
            if (originData.get(t.getName()) == null) {
                originData.put(t.getName(), 1);
            } else {
                originData.put(t.getName(), originData.get(t.getName()) + 1);
            }
        }
        List<Map.Entry<String, Integer>> list = CalculateTools.sortInteger(originData);
        for (int i = list.size() - 1; i >= (num <= list.size() ? list.size() - 20 : 0); i--) {
            Map<String, Object> map = new HashMap<>();
            map.put("keyword", list.get(i).getKey());
            map.put("weight", list.get(i).getValue());
            keywords.add(map);
        }
        return keywords;
    }

    /**
     * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
     *
     * @param term
     * @return 是否应当
     */
    public static boolean shouldInclude(Term term) {
        if (
                term.getNatureStr().startsWith("n") ||
                        term.getNatureStr().startsWith("v") ||
                        term.getNatureStr().startsWith("a")
                ) {
            // TODO 你需要自己实现一个停用词表
            if (!stopWords.contains(term.getName())) {
                return true;
            }
        }
        return false;
    }
}
