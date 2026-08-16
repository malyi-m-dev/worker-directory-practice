import java.time.LocalDate

/**
 * Точка входа. Реализует:
 *  1) ввод с клавиатуры данных объектов класса Worker;
 *  2) хранение объектов в динамической коллекции MutableList<Worker>;
 *  3) вывод фамилий работников, чей стаж превышает введённое пороговое значение;
 *  4) сообщение об отсутствии таких работников, если их нет.
 * Класс Worker и его логика вынесены в отдельный модуль — файл Worker.kt.
 */
fun main(args: Array<String>) {
    if (args.isNotEmpty() && args[0] == "--selftest") {
        SelfTests.run()
        return
    }

    println("=== Учёт сотрудников ООО «Шеринговые Технологии» (класс WORKER) ===\n")

    val workers = mutableListOf<Worker>()
    seedDemoData(workers) // демонстрационные записи, чтобы программу можно было проверить сразу

    while (true) {
        print("\nДобавить нового сотрудника? (y/n): ")
        val answer = readLine()?.trim()?.lowercase()
        if (answer != "y" && answer != "д" && answer != "да") break

        workers.add(readWorkerFromConsole())
        println("Сотрудник добавлен.")
    }

    println("\n--- Текущий список сотрудников ---")
    workers.forEach { it.display() }

    println(
        "\nВведите минимальный стаж работы в организации (лет), " +
            "чтобы вывести фамилии сотрудников, чей стаж БОЛЬШЕ этого значения:"
    )
    val threshold = readInt("Пороговое значение стажа: ", min = 0)

    val filtered = workers.filter { it.getExperience() > threshold }

    println()
    if (filtered.isEmpty()) {
        println("Работников со стажем более $threshold лет не найдено.")
    } else {
        println("Сотрудники со стажем более $threshold лет:")
        filtered.forEach { println(" - ${it.getSurnameInitials()} (стаж ${it.getExperience()} л.)") }
    }
}

private fun seedDemoData(workers: MutableList<Worker>) {
    // Демонстрация нескольких перегруженных конструкторов класса Worker.
    workers.add(Worker()) // конструктор по умолчанию
    workers.add(Worker("Туринге И.О.", "Генеральный директор")) // ФИО + должность
    workers.add(Worker("Петрова А.С.", "Backend-разработчик (Kotlin)", 180000.0)) // + зарплата
    workers.add(Worker("Сидоров К.В.", "DevOps-инженер", 210000.0, 2019)) // полный набор
}

private fun readWorkerFromConsole(): Worker {
    print("Фамилия и инициалы (например, Иванов И.И.): ")
    val surnameInitials = readLine().orEmpty()

    print("Должность: ")
    val position = readLine().orEmpty()

    val salary = readDouble("Зарплата, руб.: ")
    val hireYear = readInt("Год поступления на работу: ", min = 1950, max = LocalDate.now().year)

    return Worker(surnameInitials, position, salary, hireYear)
}

private fun readInt(prompt: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int {
    while (true) {
        print(prompt)
        val value = readLine()?.trim()?.toIntOrNull()
        if (value != null && value in min..max) return value
        println("Некорректное значение. Введите целое число от $min до $max.")
    }
}

private fun readDouble(prompt: String): Double {
    while (true) {
        print(prompt)
        val value = readLine()?.trim()?.replace(",", ".")?.toDoubleOrNull()
        if (value != null && value >= 0) return value
        println("Некорректное значение. Введите неотрицательное число.")
    }
}
