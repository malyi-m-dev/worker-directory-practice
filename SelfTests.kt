import java.time.LocalDate
import kotlin.system.exitProcess

/**
 * Простые самопроверочные тесты класса Worker без внешних зависимостей
 * (запуск: java -jar app.jar --selftest). Соответствуют пункту «Тестирование»
 * аналитического обзора (кейс-задача № 4).
 */
object SelfTests {
    private var passed = 0
    private var failed = 0

    fun run() {
        println("=== Самопроверка класса Worker ===\n")

        check("Конструктор по умолчанию задаёт нейтральные значения") {
            val w = Worker()
            w.getSurnameInitials() == "Не указано" && w.getSalary() == 0.0
        }

        check("Конструктор (ФИО, должность) обнуляет зарплату и ставит текущий год") {
            val w = Worker("Иванов И.И.", "Тестировщик")
            w.getSalary() == 0.0 && w.getHireYear() == LocalDate.now().year
        }

        check("Полный конструктор сохраняет все поля") {
            val w = Worker("Петров П.П.", "QA-инженер", 150000.0, 2020)
            w.getSurnameInitials() == "Петров П.П." &&
                w.getPosition() == "QA-инженер" &&
                w.getSalary() == 150000.0 &&
                w.getHireYear() == 2020
        }

        check("getExperience(asOfYear) считает стаж корректно") {
            val w = Worker("Сидоров С.С.", "DevOps", 200000.0, 2015)
            w.getExperience(2025) == 10
        }

        check("Методы-сеттеры изменяют поля (инкапсуляция через свойства)") {
            val w = Worker()
            w.setSurnameInitials("Кузнецова А.А.")
            w.setPosition("Продакт-менеджер")
            w.setSalary(220000.0)
            w.setHireYear(2022)
            w.getSurnameInitials() == "Кузнецова А.А." && w.getHireYear() == 2022
        }

        check("Отрицательная зарплата отклоняется исключением (валидация в сеттере)") {
            val w = Worker()
            try {
                w.setSalary(-1000.0)
                false // исключение должно было быть выброшено
            } catch (e: IllegalArgumentException) {
                true
            }
        }

        check("Конструктор копирования создаёт независимый объект") {
            val original = Worker("Орлова О.О.", "Аналитик", 175000.0, 2021)
            val copy = Worker(original)
            copy.setSalary(999999.0)
            original.getSalary() == 175000.0 && copy.getSalary() == 999999.0
        }

        println("\nИтог: пройдено $passed, провалено $failed из ${passed + failed}.")
        exitProcess(if (failed == 0) 0 else 1)
    }

    private fun check(description: String, assertion: () -> Boolean) {
        try {
            if (assertion()) {
                passed++
                println("[OK]   $description")
            } else {
                failed++
                println("[FAIL] $description")
            }
        } catch (e: Exception) {
            failed++
            println("[FAIL] $description — исключение: ${e.message}")
        }
    }
}
