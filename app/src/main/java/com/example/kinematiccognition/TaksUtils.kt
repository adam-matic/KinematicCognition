package com.example.kinematiccognition


data class Task (var type: String, var value: String, var duration: Long, var saveData: Boolean = false) {
    var startCode = 0
    var returnCode = 0
    var ActivityType : java.lang.Class<*>?
    init {
        when (type) {
            "pause" -> ActivityType = PauseActivity::class.java
            "end" -> ActivityType = PauseActivity::class.java
            "scribble" -> ActivityType = ScribbleActivity::class.java
            "trace" ->  ActivityType = TraceActivity::class.java
            "track" -> ActivityType = TrackingActivity::class.java
            else -> ActivityType = null
        }
    }


}

class TaskSequence (inList: List<Task>){
    var ls : MutableList<Task> = mutableListOf<Task>()
    companion object {
        var count: Int=0
    }

    init {
        for (l in inList) {
            var t = l.copy()
            t.startCode = count
            t.returnCode = count + 1
            count += 1
            ls.add(t)

            println("values ${t.type} ${t.value} ${t.startCode} ${t.returnCode}")
        }
        ls.last().returnCode =-1
    }
}