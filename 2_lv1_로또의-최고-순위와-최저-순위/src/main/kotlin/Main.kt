fun main(args: Array<String>) {
    val solutionClassMine = SolutionMine()
    val answerMine = solutionClassMine.solution(intArrayOf(44, 1, 0, 0, 31, 25), intArrayOf(31, 10, 45, 1, 6, 19))
    for(i in answerMine)
        print(i.toString() + " ")
    println()

    val solutionClassNotMine = SolutionNotMine()
    val answerNotMine = solutionClassNotMine.solution(intArrayOf(0, 0, 0, 0, 0, 0), intArrayOf(38, 19, 20, 40, 15, 25))
    for(i in answerNotMine)
        print(i.toString() + " ")
}

// enum class : 상수를 집합으로 관리, 따로 숫자를 할당하지 않으면 name과 ordinal을 사용해서 이름과 선언값을 알 수 있음.
enum class LottoResult(val intValue: Int){
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5),
    SIXTH(6); // semicolon 주의

    companion object{ // java의 static처럼 동작 -> 클래스명.변수로 접근 가능
        fun fromMatchCount(count: Int): LottoResult?{ // ?은 null일 수 있음을 의미, !!는 null이 아님을 의미
            return when(count){ // when: 인자가 없어도 됨, 화살표 왼쪽은 조건, 오른쪽은 조건을 만족했을 때 수행할 문장
                0, 1->SIXTH
                2->FIFTH
                3->FOURTH
                4->THIRD
                5->SECOND
                6->FIRST
                else->null // 조건과 일치하는게 없으면 else
                // switch 문과 비슷하지만 break가 필요없다.
            }
        }

        fun fromMatchCountOrThrow(count: Int): LottoResult{
            return fromMatchCount(count)
                ?: throw IllegalArgumentException("Match count not in 0..6")
                // ?:는 좌항이 null이면 우항을 수행하라는것
        }
    }
}

class SolutionNotMine {
    fun solution(lottos: IntArray, win_nums: IntArray): IntArray {
        val matchCount = (lottos.toSet() intersect win_nums.toSet()).size // union: 합집합, subtract: 차집합, intersect: 교집합
        println(matchCount)

        val unknownCount = lottos.count{it==0} // count: 조건에 일치하는 원소들의 개수 반환

        val worstResult = LottoResult.fromMatchCountOrThrow(matchCount)
        val bestResult = LottoResult.fromMatchCountOrThrow(matchCount+unknownCount)
        println(worstResult.toString() + " " + worstResult.intValue.toString())
        println(bestResult.toString() + " " + bestResult.intValue.toString())
        return intArrayOf(bestResult.intValue, worstResult.intValue)
    }
}

class SolutionMine {
    fun solution(lottos: IntArray, win_nums: IntArray): IntArray {
        // sort는 변경 가능한 list, sorted는 변경 불가능한 list
        val sortedLottos = lottos.toList().sorted()
        val sortedWinNums = win_nums.toList().sorted()

        var idx: Int = 0
        var cnt: Int = 0
        var same: Int = 0
        // for(i: Int in 0 .. 5){
        for(i: Int in 0 until 6){
            if(sortedLottos[i] == 0){
                cnt += 1
                continue
            }
            while(sortedLottos[i] > sortedWinNums[idx])
                idx += 1

            if(sortedLottos[i] == sortedWinNums[idx])
                same += 1
        }

        // 원소를 추가하기 위해서 mutable로 지정
        var answer: MutableList<Int> = mutableListOf<Int>()
        answer.add(if(same == 0 && cnt == 0) 6 else 7-(same+cnt))
        answer.add(if(same == 0) 6 else 7-same)
        return answer.toIntArray()
    }
}