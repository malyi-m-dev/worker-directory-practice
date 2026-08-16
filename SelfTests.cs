using System;

namespace WorkerDirectory
{
    /// <summary>
    /// Простые самопроверочные тесты класса Worker без внешних зависимостей
    /// (запуск: dotnet run --selftest). Соответствуют пункту «Тестирование»
    /// аналитического обзора (кейс-задача № 4).
    /// </summary>
    internal static class SelfTests
    {
        private static int passed = 0;
        private static int failed = 0;

        public static void Run()
        {
            Console.WriteLine("=== Самопроверка класса Worker ===\n");

            Check("Конструктор по умолчанию задаёт нейтральные значения",
                () =>
                {
                    var w = new Worker();
                    return w.GetSurnameInitials() == "Не указано" && w.GetSalary() == 0m;
                });

            Check("Конструктор (ФИО, должность) обнуляет зарплату и ставит текущий год",
                () =>
                {
                    var w = new Worker("Иванов И.И.", "Тестировщик");
                    return w.GetSalary() == 0m && w.GetHireYear() == DateTime.Now.Year;
                });

            Check("Полный конструктор сохраняет все поля",
                () =>
                {
                    var w = new Worker("Петров П.П.", "QA-инженер", 150000m, 2020);
                    return w.GetSurnameInitials() == "Петров П.П."
                        && w.GetPosition() == "QA-инженер"
                        && w.GetSalary() == 150000m
                        && w.GetHireYear() == 2020;
                });

            Check("GetExperience(asOfYear) считает стаж корректно",
                () =>
                {
                    var w = new Worker("Сидоров С.С.", "DevOps", 200000m, 2015);
                    return w.GetExperience(2025) == 10;
                });

            Check("Методы-сеттеры изменяют поля (инкапсуляция через свойства)",
                () =>
                {
                    var w = new Worker();
                    w.SetSurnameInitials("Кузнецова А.А.");
                    w.SetPosition("Продакт-менеджер");
                    w.SetSalary(220000m);
                    w.SetHireYear(2022);
                    return w.GetSurnameInitials() == "Кузнецова А.А." && w.GetHireYear() == 2022;
                });

            Check("Отрицательная зарплата отклоняется исключением (валидация в сеттере)",
                () =>
                {
                    var w = new Worker();
                    try
                    {
                        w.SetSalary(-1000m);
                        return false; // исключение должно было быть выброшено
                    }
                    catch (ArgumentException)
                    {
                        return true;
                    }
                });

            Check("Конструктор копирования создаёт независимый объект",
                () =>
                {
                    var original = new Worker("Орлова О.О.", "Аналитик", 175000m, 2021);
                    var copy = new Worker(original);
                    copy.SetSalary(999999m);
                    return original.GetSalary() == 175000m && copy.GetSalary() == 999999m;
                });

            Console.WriteLine($"\nИтог: пройдено {passed}, провалено {failed} из {passed + failed}.");
            Environment.ExitCode = failed == 0 ? 0 : 1;
        }

        private static void Check(string description, Func<bool> assertion)
        {
            try
            {
                bool result = assertion();
                if (result)
                {
                    passed++;
                    Console.WriteLine($"[OK]   {description}");
                }
                else
                {
                    failed++;
                    Console.WriteLine($"[FAIL] {description}");
                }
            }
            catch (Exception ex)
            {
                failed++;
                Console.WriteLine($"[FAIL] {description} — исключение: {ex.Message}");
            }
        }
    }
}
