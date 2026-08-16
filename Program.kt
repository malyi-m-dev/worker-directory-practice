import java.time.LocalDate

/**
 * Точка входа. Реализует:
 *  1) ввод с клавиатуры данных объектов класса Worker (единиц парка СИМ);
 *  2) хранение объектов в динамической коллекции MutableList<Worker>;
 *  3) вывод бортовых номеров единиц парка, чей срок эксплуатации превышает
 *     введённое пороговое значение (кандидаты на списание/замену);
 *  4) сообщение об отсутствии таких единиц, если их нет.
 * Класс Worker и его логика вынесены в отдельный модуль — файл Worker.kt.
 */
fun main(args: Array<String>) {
    if (args.isNotEmpty() && args[0] == "--selftest") {
        SelfTests.run()
        return
    }

    println("=== Учёт парка средств индивидуальной мобильности ООО «Шеринговые Технологии» (класс WORKER) ===\n")

    val fleet = mutableListOf<Worker>()
    seedDemoData(fleet) // демонстрационные записи, чтобы программу можно было проверить сразу

    while (true) {
        print("\nДобавить единицу парка? (y/n): ")
        val answer = readLine()?.trim()?.lowercase()
        if (answer != "y" && answer != "д" && answer != "да") break

        fleet.add(readWorkerFromConsole())
        println("Единица парка добавлена.")
    }

    println("\n--- Текущий состав парка ---")
    fleet.forEach { it.display() }

    println(
        "\nВведите минимальный срок эксплуатации (лет), чтобы вывести бортовые номера " +
            "единиц парка, чей срок эксплуатации БОЛЬШЕ этого значения (кандидаты на списание):"
    )
    val threshold = readInt("Пороговое значение срока эксплуатации: ", min = 0)

    val filtered = fleet.filter { it.getServiceYears() > threshold }

    println()
    if (filtered.isEmpty()) {
        println("Единиц парка со сроком эксплуатации более $threshold лет не найдено.")
    } else {
        println("Единицы парка со сроком эксплуатации более $threshold лет (кандидаты на списание):")
        filtered.forEach { println(" - ${it.getInventoryNumber()} (в парке ${it.getServiceYears()} л.)") }
    }
}

private fun seedDemoData(fleet: MutableList<Worker>) {
    // Демонстрация нескольких перегруженных конструкторов класса Worker.
    fleet.add(Worker()) // конструктор по умолчанию
    fleet.add(Worker("Юрент-014522", "Электросамокат Юрент 2.0")) // номер + тип
    fleet.add(Worker("Юрент-021390", "Электровелосипед Юрент", 65000.0)) // + стоимость
    fleet.add(Worker("Юрент-008117", "Электросамокат Ninebot Max (партнёрский)", 48000.0, 2020)) // полный набор
}

private fun readWorkerFromConsole(): Worker {
    print("Бортовой (инвентарный) номер (например, Юрент-014522): ")
    val inventoryNumber = readLine().orEmpty()

    print("Тип транспортного средства (самокат/велосипед/павербанк, модель): ")
    val vehicleType = readLine().orEmpty()

    val bookValue = readDouble("Балансовая стоимость, руб.: ")
    val commissionedYear = readInt("Год ввода в эксплуатацию: ", min = 2018, max = LocalDate.now().year)

    return Worker(inventoryNumber, vehicleType, bookValue, commissionedYear)
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
