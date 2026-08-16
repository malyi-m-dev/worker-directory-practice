using System;

namespace WorkerDirectory
{
    /// <summary>
    /// Класс WORKER описывает сотрудника подразделения информационных технологий
    /// ООО «Шеринговые Технологии» (группа МТС Юрент).
    /// Содержит фамилию и инициалы, должность, зарплату и год поступления на работу.
    /// Инкапсулирует данные: поля закрыты (private), доступ — только через свойства
    /// и методы класса.
    /// </summary>
    public class Worker
    {
        // ------------------- Поля (инкапсулированы) -------------------
        private string surnameInitials;   // фамилия и инициалы, например "Иванов И.И."
        private string position;          // название занимаемой должности
        private decimal salary;           // зарплата, руб.
        private int hireYear;             // год поступления на работу

        // ------------------- Свойства с валидацией -------------------
        public string SurnameInitials
        {
            get => surnameInitials;
            set => surnameInitials = string.IsNullOrWhiteSpace(value)
                ? throw new ArgumentException("Фамилия и инициалы не могут быть пустыми.")
                : value.Trim();
        }

        public string Position
        {
            get => position;
            set => position = string.IsNullOrWhiteSpace(value)
                ? throw new ArgumentException("Должность не может быть пустой.")
                : value.Trim();
        }

        public decimal Salary
        {
            get => salary;
            set => salary = value < 0
                ? throw new ArgumentException("Зарплата не может быть отрицательной.")
                : value;
        }

        public int HireYear
        {
            get => hireYear;
            set => hireYear = (value < 1950 || value > DateTime.Now.Year)
                ? throw new ArgumentException($"Год поступления должен быть в диапазоне 1950–{DateTime.Now.Year}.")
                : value;
        }

        // ------------------- Конструкторы -------------------

        /// <summary>Конструктор по умолчанию.</summary>
        public Worker()
        {
            surnameInitials = "Не указано";
            position = "Не указано";
            salary = 0m;
            hireYear = DateTime.Now.Year;
        }

        /// <summary>Конструктор с частичными параметрами (ФИО и должность).</summary>
        public Worker(string surnameInitials, string position)
            : this(surnameInitials, position, 0m, DateTime.Now.Year)
        {
        }

        /// <summary>Конструктор с параметрами ФИО, должность, зарплата.</summary>
        public Worker(string surnameInitials, string position, decimal salary)
            : this(surnameInitials, position, salary, DateTime.Now.Year)
        {
        }

        /// <summary>Полный конструктор со всеми параметрами.</summary>
        public Worker(string surnameInitials, string position, decimal salary, int hireYear)
        {
            SurnameInitials = surnameInitials;
            Position = position;
            Salary = salary;
            HireYear = hireYear;
        }

        /// <summary>Конструктор копирования — создаёт независимую копию объекта.</summary>
        public Worker(Worker other)
        {
            if (other is null) throw new ArgumentNullException(nameof(other));
            surnameInitials = other.surnameInitials;
            position = other.position;
            salary = other.salary;
            hireYear = other.hireYear;
        }

        // ------------------- Деструктор (финализатор) -------------------
        // В C# память управляется сборщиком мусора, явный вызов деструктора
        // не требуется и не гарантирован по времени. Финализатор оставлен
        // как демонстрация синтаксиса деструктора, предусмотренного заданием,
        // и используется только для трассировки уничтожения объекта.
        ~Worker()
        {
            // В боевом коде тело финализатора обычно пустое либо освобождает
            // неуправляемые ресурсы (файлы, сокеты и т.п.), которых у Worker нет.
        }

        // ------------------- Методы изменения полей -------------------
        public void SetSurnameInitials(string value) => SurnameInitials = value;
        public void SetPosition(string value) => Position = value;
        public void SetSalary(decimal value) => Salary = value;
        public void SetHireYear(int value) => HireYear = value;

        public void ChangeData(string surnameInitials, string position, decimal salary, int hireYear)
        {
            SurnameInitials = surnameInitials;
            Position = position;
            Salary = salary;
            HireYear = hireYear;
        }

        // ------------------- Методы отображения полей -------------------
        public string GetSurnameInitials() => surnameInitials;
        public string GetPosition() => position;
        public decimal GetSalary() => salary;
        public int GetHireYear() => hireYear;

        /// <summary>Выводит данные о работнике на консоль в читаемом виде.</summary>
        public void Display()
        {
            Console.WriteLine(
                $"{SurnameInitials,-20} | {Position,-25} | {Salary,10:N2} руб. | принят(а) в {HireYear} г. | стаж {GetExperience()} л.");
        }

        // ------------------- Метод, требуемый условием задачи -------------------

        /// <summary>Стаж работы в организации в полных годах на текущий момент.</summary>
        public int GetExperience() => DateTime.Now.Year - hireYear;

        /// <summary>Стаж работы в организации на указанный год (для тестов/расчётов).</summary>
        public int GetExperience(int asOfYear) => asOfYear - hireYear;

        public override string ToString() =>
            $"{SurnameInitials} — {Position}, стаж {GetExperience()} л.";
    }
}
