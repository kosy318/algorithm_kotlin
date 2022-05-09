fun main(args: Array<String>) {
    val SolutionClass1 = Solution1()
    println(SolutionClass1.solution("...!@BaT#*..y.abcdefghijklm"))

    val SolutionClass2 = Solution2()
    println(SolutionClass2.solution("=.="))
}

class Solution1 {
    fun solution(new_id: String): String = new_id
        // 2단계 알파벳, 숫자, 빼기 밑줄, 마침표를 제외한 모든 문자를 제거
        // isLetterOrDigit(), isLetter(), isDigit() 다있음
        .filter{it.isLetterOrDigit() || it.toString() == "-" || it.toString() == "_" || it.toString() == "."}
        .map{it.toLowerCase()} //.toString()} toString()해봤자 List임
        // .flatMap{it.toList()} // 1대 다 매핑이 가능, 근데 map까지 수행하고 이미 List 타입이라 필요없어보임
        // let, with, run, apply, also 비슷비슷한게 많음..
        // joinToString(구분자, 시작, 종료) 형식으로 사용, 배열 데이터를 string으로 바꿀 때 사용
        .let{it.joinToString("")}
        // \\. 은 마침표를 의미, {2,}는 2개 이상을 의미
        .let{it.replace("\\.{2,}".toRegex(), ".")}
        // 4단계 마침표가 처음이나 끝에 위치해 있다면, 제거
        .removePrefix(".").removeSuffix(".")
        .let{ // 5단계 비었으면 "a"를 리턴 아니면 원래 it을 리턴
            if(it.isEmpty())
                "a"
            else it // 이게 없어도 되지 않을까 했는데 아니었다.
        }
        .let{
            if(it.length >= 16) // 6단계, 원래 removePrefix도 적혀있었지만, 앞에서 제거해서 필요없다고 판단.
            // .toString()이 두개 들어있었는데 없어도 되는거같아 지웠더니 된다.
                it.removeRange(15, it.length).removeSuffix(".")
            else it
        }
        .let{
            if(it.length <= 2){ // 7단계
                when(it.length){
                    // 원래 문자열(여기서는 한글자)을 3번 반복
                    // 원래 "${it.get(0)}${it.last()}${it.last()}" 라고 적혀있었음, get(0)를 하면 0위치의 값을 리턴하는것같다.
                    1->"$it$it$it" // it만 쓸거 아니면 중괄호 필수
                    // it.last()를 하면 마지막 위치의 값을 리턴하는것같다.
                    else->"$it${it.last()}"
                }
            } else it
        }
    // .javaClass.name // 데이터 타입 확인하려고 사용했었다.
}

class Solution2 {
    fun solution(new_id: String): String {
        var answer: String = new_id

        // 1단계 대문자 to 소문자
        answer = answer.toLowerCase() // 대입을 해줘야한다. string은 immutable해서?

        val reg2 = Regex("[a-z0-9-_.]") // 2단계 regex 소문자, 숫자, 빼기, 마침표만 골라냄

        var temp = StringBuilder() // 속도가 string보다 빠르다, single thread 환경, 그 다음은 StringBuffer임, multi thread 환경
        /*
        for(i in answer.indices){ // indices는 index의 값을 가져온다 0, 1, ...
            // https://codechacha.com/ko/kotlin-how-to-use-regex/
            // regex.containsMatchIn(str): 부분적으로 일치하면 true를 리턴
            // regex.matches(str): 모두 일치하면 true를 리턴
            if(reg2.containsMatchIn(answer[i].toString())){
                temp.append(answer[i])
            }
        }
        answer = temp.toString()
        */

        // 위 사이트에서 검색을 하고 보니 아래와 같은 방법도 가능할것같아 테스트 해봤다. 굳
        val matchResults: Sequence<MatchResult> = reg2.findAll(answer)
        answer = matchResults.map{it.value}.joinToString("")

        val reg3 = Regex("[.]{2,}") // 3단계 regex .이 두개 이상
        answer = answer.replace(reg3, ".")

        // 4단계 처음과 끝 .제거
        // 비어있지 않고 첫번째 문자가 .이면
        if(answer.isNotEmpty() && answer.first() == '.') answer = answer.removeRange(0, 1)
        // 비어있지 않고 마지막 문자가 .이면
        if(answer.isNotEmpty() && answer.last() == '.') answer = answer.removeRange(answer.length-1, answer.length)

        // 5단계
        if(answer.isEmpty()) answer = "a"

        // 6단계
        if(answer.length >= 16) {
            answer = answer.substring(0, 15) // 0~14만 잘라서 넣음

            if(answer.last() == '.') // 마지막 문자가 .이면 제거
                answer = answer.removeRange(answer.length-1, answer.length)
        }

        // 7단계
        while(answer.length <= 2){
            // 여러가지 방법 존재, 난 마지막것이 좋다.
            // answer = answer.plus(answer[answer.length-1])
            // answer += answer[answer.length-1]
            answer += answer.last()
        }

        return answer
    }
}