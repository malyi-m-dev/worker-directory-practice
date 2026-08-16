import java.time.LocalDate

/**
 * Класс WORKER описывает сотрудника подразделения информационных технологий
 * ООО «Шеринговые Технологии» (группа МТС, платформа «Юрент»).
 * Содержит фамилию и инициалы, должность, зарплату и год поступления на работу.
 * Данные инкапсулированы: поля закрыты (private), доступ — только через
 * явные методы изменения и отображения, выполняющие валидацию.
 */
class Worker {

    // ------------------- Поля (инкапсулированы) -------------------
    private var surnameInitials: String = "Не указано"
    private var position: String = "Не указано"
    private var salary: Double = 0.0
    private var hireYear: Int = LocalDate.now().year

    // ------------------- Конструкторы -------------------

    /** Конструктор по умолчанию. */
    constructor()

    /** Конструктор с частичными параметрами (ФИО и должность). */
    constructor(surnameInitials: String, position: String) : this() {
        setSurnameInitials(surnameInitials)
        setPosition(position)
    }

    /** Конструктор с параметрами ФИО, должность, зарплата. */
    constructor(surnameInitials: String, position: String, salary: Double)
        : this(surnameInitials, position) {
        setSalary(salary)
    }

    /** Полный конструктор со всеми параметрами. */
    constructor(surnameInitials: String, position: String, salary: Double, hireYear: Int)
        : this(surnameInitials, position, salary) {
        setHireYear(hireYear)
    }

    /** Конструктор копирования — создаёт независимую копию объекта. */
    constructor(other: Worker) : this(other.surnameInitials, other.position, other.salary, other.hireYear)

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
    fun setSurnameInitials(value: String) {
        surnameInitials = if (value.isBlank())
            throw IllegalArgumentException("Фамилия и инициалы не могут быть пустыми.")
        else value.trim()
    }

    fun setPosition(value: String) {
        position = if (value.isBlank())
            throw IllegalArgumentException("Должность не может быть пустой.")
        else value.trim()
    }

    fun setSalary(value: Double) {
        salary = if (value < 0)
            throw IllegalArgumentException("Зарплата не может быть отрицательной.")
        else value
    }

    fun setHireYear(value: Int) {
        val currentYear = LocalDate.now().year
        hireYear = if (value < 1950 || value > currentYear)
            throw IllegalArgumentException("Год поступления должен быть в диапазоне 1950–$currentYear.")
        else value
    }

    fun changeData(surnameInitials: String, position: String, salary: Double, hireYear: Int) {
        setSurnameInitials(surnameInitials)
        setPosition(position)
        setSalary(salary)
        setHireYear(hireYear)
    }

    // ------------------- Методы отображения полей -------------------
    fun getSurnameInitials(): String = surnameInitials
    fun getPosition(): String = position
    fun getSalary(): Double = salary
    fun getHireYear(): Int = hireYear

    /** Выводит данные о работнике на консоль в читаемом виде. */
    fun display() {
        println(
            "%-20s | %-25s | %10.2f руб. | принят(а) в %d г. | стаж %d л."
                .format(surnameInitials, position, salary, hireYear, getExperience())
        )
    }

    // ------------------- Метод, требуемый условием задачи -------------------

    /** Стаж работы в организации в полных годах на указанный (по умолчанию — текущий) год. */
    fun getExperience(asOfYear: Int = LocalDate.now().year): Int = asOfYear - hireYear

    override fun toString(): String = "$surnameInitials — $position, стаж ${getExperience()} л."
}
