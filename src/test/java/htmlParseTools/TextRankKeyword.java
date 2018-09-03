package htmlParseTools;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * TextRank关键词提取
 * @author hankcs
 */
public class TextRankKeyword
{
    public static final int nKeyword = 10;
    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    static final float d = 0.85f;
    /**
     * 最大迭代次数
     */
    static final int max_iter = 200;
    static final float min_diff = 0.001f;

    public TextRankKeyword()
    {
        // jdk bug : Exception in thread "main" java.lang.IllegalArgumentException: Comparison method violates its general contract!
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public String getKeyword(String title, String content)
    {
        List<Term> termList = ToAnalysis.parse(title + content);
        List<String> wordList = new ArrayList<String>();
        for (Term t : termList)
        {
            if (shouldInclude(t))
            {
                wordList.add(t.getName());
            }
        }
        Map<String, Set<String>> words = new HashMap<String, Set<String>>();
        Queue<String> que = new LinkedList<String>();
        for (String w : wordList)
        {
            if (!words.containsKey(w))
            {
                words.put(w, new HashSet<String>());
            }
            que.offer(w);
            if (que.size() > 5)
            {
                que.poll();
            }

            for (String w1 : que)
            {
                for (String w2 : que)
                {
                    if (w1.equals(w2))
                    {
                        continue;
                    }

                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
            }
        }
        System.out.println("分组结果："+words);
        Map<String, Float> score = new HashMap<String, Float>();
        for (int i = 0; i < max_iter; ++i)
        {
            Map<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet())
            {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String other : value)
                {
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) break;
        }
        List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(score.entrySet());
        Collections.sort(entryList, (o1, o2) -> (o1.getValue() - o2.getValue() > 0 ? -1 : 1));
        String result = "";
        for (int i = 0; i < nKeyword; ++i)
        {
            result += entryList.get(i).getKey() + ','+entryList.get(i).getValue()+"#";
        }
        return result;
    }

    public static void main(String[] args)
    {
        String content = "自然语言处理是计算机科学领域与人工智能领域中的一个重要方向。它研究能实现人与计算机之间用自然语言进行有效通信的各种理论和方法。" +
                "自然语言处理是一门融语言学、计算机科学、数学于一体的科学。因此，这一领域的研究将涉及自然语言，即人们日常使用的语言，所以它与语言学的研究有" +
                "着密切的联系，但又有重要的区别。自然语言处理并不是一般地研究自然语言，而在于研制能有效地实现自然语言通信的计算机系统，特别是其中的软件系统。" +
                "因而它是计算机科学的一部分。" +
                "自然语言处理（NLP）是计算机科学，人工智能，语言学关注计算机和人类（自然）语言之间的相互作用的领域";
        System.out.println(new TextRankKeyword().getKeyword("", content));

    }

    public Set getStopWordDic(){
        Set<String> stopwords = new HashSet<String>();
        try {
            BufferedReader inBufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("./my_stop_words.txt"), Charset.forName("UTF-8")));
            String line;

            while((line = inBufferedReader.readLine()) != null)
            {
                line = line.trim();
                if(!stopwords.contains(line))
                {
                    stopwords.add(line);

                }
            }
            inBufferedReader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        return stopwords;
    }
    /**
     * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
     * @param term
     * @return 是否应当
     */
    public boolean shouldInclude(Term term)
    {
        Set<String> StopWordDictionary=getStopWordDic();
//        System.out.println(StopWordDictionary);
        if (
                term.getNatureStr().startsWith("n") ||
                term.getNatureStr().startsWith("v") ||
                term.getNatureStr().startsWith("d") ||
                term.getNatureStr().startsWith("a")
                )
        {
            // TODO 你需要自己实现一个停用词表
            if (!StopWordDictionary.contains(term.getName()))
            {
                return true;
            }
        }

        return false;
    }
}
