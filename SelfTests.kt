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
        println("=== Самопроверка класса Worker (учёт парка СИМ) ===\n")

        check("Конструктор по умолчанию задаёт нейтральные значения") {
            val w = Worker()
            w.getInventoryNumber() == "Не указан" && w.getBookValue() == 0.0
        }

        check("Конструктор (номер, тип ТС) обнуляет стоимость и ставит текущий год") {
            val w = Worker("Юрент-000001", "Электросамокат")
            w.getBookValue() == 0.0 && w.getCommissionedYear() == LocalDate.now().year
        }

        check("Полный конструктор сохраняет все поля") {
            val w = Worker("Юрент-000777", "Электровелосипед", 65000.0, 2021)
            w.getInventoryNumber() == "Юрент-000777" &&
                w.getVehicleType() == "Электровелосипед" &&
                w.getBookValue() == 65000.0 &&
                w.getCommissionedYear() == 2021
        }

        check("getServiceYears(asOfYear) считает срок эксплуатации корректно") {
            val w = Worker("Юрент-000555", "Электросамокат", 40000.0, 2020)
            w.getServiceYears(2025) == 5
        }

        check("Методы-сеттеры изменяют поля (инкапсуляция через методы)") {
            val w = Worker()
            w.setInventoryNumber("Юрент-999999")
            w.setVehicleType("Павербанк")
            w.setBookValue(3500.0)
            w.setCommissionedYear(2023)
            w.getInventoryNumber() == "Юрент-999999" && w.getCommissionedYear() == 2023
        }

        check("Отрицательная стоимость отклоняется исключением (валидация в сеттере)") {
            val w = Worker()
            try {
                w.setBookValue(-1000.0)
                false // исключение должно было быть выброшено
            } catch (e: IllegalArgumentException) {
                true
            }
        }

        check("Конструктор копирования создаёт независимый объект") {
            val original = Worker("Юрент-000321", "Электросамокат", 45000.0, 2022)
            val copy = Worker(original)
            copy.setBookValue(1.0)
            original.getBookValue() == 45000.0 && copy.getBookValue() == 1.0
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
