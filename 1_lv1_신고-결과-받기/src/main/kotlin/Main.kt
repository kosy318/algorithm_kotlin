fun main(args: Array<String>) {
    val solutionClass = Solution()
    val answer = solutionClass.solution(arrayOf("muzi", "frodo", "apeach", "neo"), arrayOf("muzi frodo","apeach frodo","frodo neo","muzi neo","apeach muzi"), 2)
    for(i in answer)
        print(i.toString() + " ")
}

class Solution {
    fun solution(id_list: Array<String>, report: Array<String>, k: Int): IntArray {
        val reporterAndTarget = report.map{val reporterId = it.substringBefore(' ')
            val targetId = it.substringAfter(' ')
            Pair(reporterId, targetId)}.distinct()
        // 한 유저를여러번 신고할 수 있지만, 동일한 유저에 대한 신고 횟수는 1회로 처리된다. => distinct()

        for((reporter, target) in reporterAndTarget)
            println(reporter + " " + target)

        // groupingBy는 groupBy + 연산하는 용도
        // targetId별로 grouping
        val suspendedId = reporterAndTarget.groupingBy{(_, targetId) -> targetId}
            .eachCount() // 각 요소를 count해서
            .filter{it.value >= k} // k이상인 것들을 filtering
            .keys // 그것의 key값들을 대입

        println(suspendedId)

        // targetId가 suspendedId에 속하는것을 filtering
        val reporterIdToSendMail = reporterAndTarget.filter{(_, targetId) -> targetId in suspendedId}
            // reporter로 grouping
            .groupingBy{(reporterId, _) -> reporterId}
            .eachCount() // reporter가 메일 받는 횟수

        println(reporterIdToSendMail)

        // getOrDefault : 지정된 키에 해당하는 값을 반환하거나 이러한 키가 map에 없는 경우 defaultValue를 반환
        return id_list.map{reporterIdToSendMail.getOrDefault(it, 0)} // 여기까진 List
                      .toIntArray() // 이걸 해줘야 IntArray가 됨
    }
}