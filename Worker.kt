import java.time.LocalDate

/**
 * Класс WORKER адаптирован под профильную деятельность ООО «Шеринговые Технологии»
 * (платформа «Юрент», группа МТС): моделирует единицу подвижного состава парка
 * средств индивидуальной мобильности (электросамокат, электровелосипед, павербанк) —
 * тот актив, вокруг которого построен весь сервис компании.
 *
 * Поля исходного задания перенесены на предметную область самокатного парка
 * по прямой аналогии:
 *   фамилия и инициалы работника  → бортовой (инвентарный) номер самоката
 *   название занимаемой должности → тип/модель транспортного средства
 *   зарплата                      → балансовая стоимость единицы, руб.
 *   год поступления на работу     → год ввода в эксплуатацию
 *   стаж работы в организации     → срок эксплуатации (лет) — см. getServiceYears()
 *
 * Данные инкапсулированы: поля закрыты (private), доступ — только через
 * явные методы изменения и отображения, выполняющие валидацию.
 */
class Worker {

    // ------------------- Поля (инкапсулированы) -------------------
    private var inventoryNumber: String = "Не указан"
    private var vehicleType: String = "Не указан"
    private var bookValue: Double = 0.0
    private var commissionedYear: Int = LocalDate.now().year

    // ------------------- Конструкторы -------------------

    /** Конструктор по умолчанию. */
    constructor()

    /** Конструктор с частичными параметрами (бортовой номер и тип ТС). */
    constructor(inventoryNumber: String, vehicleType: String) : this() {
        setInventoryNumber(inventoryNumber)
        setVehicleType(vehicleType)
    }

    /** Конструктор с параметрами: бортовой номер, тип ТС, балансовая стоимость. */
    constructor(inventoryNumber: String, vehicleType: String, bookValue: Double)
        : this(inventoryNumber, vehicleType) {
        setBookValue(bookValue)
    }

    /** Полный конструктор со всеми параметрами. */
    constructor(inventoryNumber: String, vehicleType: String, bookValue: Double, commissionedYear: Int)
        : this(inventoryNumber, vehicleType, bookValue) {
        setCommissionedYear(commissionedYear)
    }

    /** Конструктор копирования — создаёт независимую копию объекта. */
    constructor(other: Worker) : this(other.inventoryNumber, other.vehicleType, other.bookValue, other.commissionedYear)

    // ------------------- Деструктор -------------------
    // На платформе JVM память управляется сборщиком мусора, объекты не имеют
    // детерминированного деструктора, как в C++. Исторический аналог —
    // переопределение метода Object.finalize(), но он объявлен устаревшим
    // начиная с Java 9 и не гарантирует момент вызова. Метод оставлен только
    // как учебная демонстрация синтаксиса деструктора, требуемого заданием;
    // для освобождения реальных ресурсов в Kotlin/JVM используют интерфейс
    // AutoCloseable и блок use { ... } (см. кейс-задачу № 5).
    @Suppress("DEPRECATION", "removal")
    protected fun finalize() {
        // Тело намеренно пустое: у Worker нет неуправляемых ресурсов.
    }

    // ------------------- Методы изменения полей -------------------
    fun setInventoryNumber(value: String) {
        inventoryNumber = if (value.isBlank())
            throw IllegalArgumentException("Бортовой номер не может быть пустым.")
        else value.trim()
    }

    fun setVehicleType(value: String) {
        vehicleType = if (value.isBlank())
            throw IllegalArgumentException("Тип транспортного средства не может быть пустым.")
        else value.trim()
    }

    fun setBookValue(value: Double) {
        bookValue = if (value < 0)
            throw IllegalArgumentException("Балансовая стоимость не может быть отрицательной.")
        else value
    }

    fun setCommissionedYear(value: Int) {
        val currentYear = LocalDate.now().year
        // 2018 — год запуска кикшеринга компанией (см. кейс-задачу № 1),
        // раньше этого года единиц парка электросамокатов быть не может.
        commissionedYear = if (value < 2018 || value > currentYear)
            throw IllegalArgumentException("Год ввода в эксплуатацию должен быть в диапазоне 2018–$currentYear.")
        else value
    }

    fun changeData(inventoryNumber: String, vehicleType: String, bookValue: Double, commissionedYear: Int) {
        setInventoryNumber(inventoryNumber)
        setVehicleType(vehicleType)
        setBookValue(bookValue)
        setCommissionedYear(commissionedYear)
    }

    // ------------------- Методы отображения полей -------------------
    fun getInventoryNumber(): String = inventoryNumber
    fun getVehicleType(): String = vehicleType
    fun getBookValue(): Double = bookValue
    fun getCommissionedYear(): Int = commissionedYear

    /** Выводит данные о единице парка на консоль в читаемом виде. */
    fun display() {
        println(
            "%-16s | %-30s | %10.2f руб. | введён в эксплуатацию в %d г. | в парке %d л."
                .format(inventoryNumber, vehicleType, bookValue, commissionedYear, getServiceYears())
        )
    }

    // ------------------- Метод, требуемый условием задачи -------------------

    /** Срок эксплуатации в полных годах на указанный (по умолчанию — текущий) год. */
    fun getServiceYears(asOfYear: Int = LocalDate.now().year): Int = asOfYear - commissionedYear

    override fun toString(): String = "$inventoryNumber — $vehicleType, в парке ${getServiceYears()} л."
}
