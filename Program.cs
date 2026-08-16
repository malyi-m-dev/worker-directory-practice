using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;

namespace WorkerDirectory
{
    /// <summary>
    /// Точка входа. Реализует:
    ///  1) ввод с клавиатуры данных объектов класса Worker;
    ///  2) хранение объектов в динамической коллекции List&lt;Worker&gt;;
    ///  3) вывод фамилий работников, чей стаж превышает введённое пороговое значение;
    ///  4) сообщение об отсутствии таких работников, если их нет.
    /// Класс Worker и его логика вынесены в отдельный модуль — файл Worker.cs.
    /// </summary>
    internal static class Program
    {
        private static void Main(string[] args)
        {
            if (args.Length > 0 && args[0] == "--selftest")
            {
                SelfTests.Run();
                return;
            }

            Console.OutputEncoding = System.Text.Encoding.UTF8;
            Console.WriteLine("=== Учёт сотрудников ООО «Шеринговые Технологии» (класс WORKER) ===\n");

            List<Worker> workers = new List<Worker>();
            SeedDemoData(workers); // несколько демонстрационных записей, чтобы программу можно было проверить сразу

            bool inputMore = true;
            while (inputMore)
            {
                Console.WriteLine("\nДобавить нового сотрудника? (y/n)");
                string answer = Console.ReadLine()?.Trim().ToLowerInvariant();
                if (answer != "y" && answer != "д" && answer != "да")
                {
                    inputMore = false;
                    break;
                }

                Worker worker = ReadWorkerFromConsole();
                workers.Add(worker);
                Console.WriteLine("Сотрудник добавлен.");
            }

            Console.WriteLine("\n--- Текущий список сотрудников ---");
            foreach (Worker w in workers)
            {
                w.Display();
            }

            Console.WriteLine("\nВведите минимальный стаж работы в организации (лет), " +
                               "чтобы вывести фамилии сотрудников, чей стаж БОЛЬШЕ этого значения:");
            int threshold = ReadInt("Пороговое значение стажа: ", allowNegative: false);

            List<Worker> filtered = workers.Where(w => w.GetExperience() > threshold).ToList();

            Console.WriteLine();
            if (filtered.Count == 0)
            {
                Console.WriteLine($"Работников со стажем более {threshold} лет не найдено.");
            }
            else
            {
                Console.WriteLine($"Сотрудники со стажем более {threshold} лет:");
                foreach (Worker w in filtered)
                {
                    Console.WriteLine($" - {w.GetSurnameInitials()} (стаж {w.GetExperience()} л.)");
                }
            }
        }

        private static void SeedDemoData(List<Worker> workers)
        {
            // Демонстрация нескольких перегруженных конструкторов класса Worker.
            workers.Add(new Worker()); // конструктор по умолчанию
            workers.Add(new Worker("Туринге И.О.", "Генеральный директор")); // ФИО + должность
            workers.Add(new Worker("Петрова А.С.", "Backend-разработчик (.NET)", 180000m)); // + зарплата
            workers.Add(new Worker("Сидоров К.В.", "DevOps-инженер", 210000m, 2019)); // полный набор
        }

        private static Worker ReadWorkerFromConsole()
        {
            Console.Write("Фамилия и инициалы (например, Иванов И.И.): ");
            string surnameInitials = Console.ReadLine();

            Console.Write("Должность: ");
            string position = Console.ReadLine();

            decimal salary = ReadDecimal("Зарплата, руб.: ");
            int hireYear = ReadInt("Год поступления на работу: ", allowNegative: false, min: 1950, max: DateTime.Now.Year);

            return new Worker(surnameInitials, position, salary, hireYear);
        }

        private static int ReadInt(string prompt, bool allowNegative, int min = int.MinValue, int max = int.MaxValue)
        {
            while (true)
            {
                Console.Write(prompt);
                string raw = Console.ReadLine();
                if (int.TryParse(raw, out int value) && (allowNegative || value >= 0) && value >= min && value <= max)
                {
                    return value;
                }
                Console.WriteLine($"Некорректное значение. Введите целое число от {Math.Max(min, allowNegative ? int.MinValue : 0)} до {max}.");
            }
        }

        private static decimal ReadDecimal(string prompt)
        {
            while (true)
            {
                Console.Write(prompt);
                string raw = Console.ReadLine();
                if (decimal.TryParse(raw, NumberStyles.Number, CultureInfo.InvariantCulture, out decimal value) && value >= 0)
                {
                    return value;
                }
                if (decimal.TryParse(raw, NumberStyles.Number, CultureInfo.GetCultureInfo("ru-RU"), out value) && value >= 0)
                {
                    return value;
                }
                Console.WriteLine("Некорректное значение. Введите неотрицательное число.");
            }
        }
    }
}
