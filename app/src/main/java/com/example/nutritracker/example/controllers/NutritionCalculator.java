package com.example.nutritracker.example.controllers;

import com.example.nutritracker.example.models.Meal;

import java.util.List;

public class NutritionCalculator {

    public static Meal calculateNextBestMeal(List<Meal> lastFiveMeals, double weightKg) {
        double totalCalories = 0;
        double totalFats = 0;
        double totalCarbs = 0;
        double totalSugars = 0;
        double totalProtein = 0;

        for (Meal meal : lastFiveMeals) {
            totalCalories += meal.calories;
            totalFats += meal.fats;
            totalCarbs += meal.carbs;
            totalSugars += meal.sugars;
            totalProtein += meal.protein;
        }

        double avgCalories = totalCalories / 5.0;
        double avgFats = totalFats / 5.0;
        double avgCarbs = totalCarbs / 5.0;
        double avgSugars = totalSugars / 5.0;
        double avgProtein = totalProtein / 5.0;

        double weightFactor = weightKg / 70.0;

        return new Meal(
                (int) Math.round(avgCalories * weightFactor),
                avgFats * weightFactor,
                avgCarbs * weightFactor,
                avgSugars * weightFactor,
                avgProtein * weightFactor
        );
    }
}
