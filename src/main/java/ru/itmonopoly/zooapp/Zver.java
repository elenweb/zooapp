package ru.itmonopoly.zooapp;

import java.io.*;

public class Zver implements Serializable {
    String name;
    Zver parent;
    Boolean type; // true - zver; false - question
    Zver yes;
    Zver no;

    public Zver(String name, Zver parent, Boolean type) { //конструктор
        this.name = name;
        this.parent = parent;
        this.type = type;
    }

    @Override
    public String toString() {
        if (this == this.parent.no) {
            return this.name + ", " + this.parent.name + ".no";
        } else {
            if (this == this.parent.yes) {
                return this.name + ", " + this.parent.name + ".yes";
            }
        }
        return this.name + ", " + this.parent.name;
    }

    public boolean equals(Zver z) {
        boolean b = true;
        if (z.name == this.name && z.parent == this.parent && z.type == this.type) {
            return b;
        } else {
            b = false;
            return b;
        }
    }

    public void game(Zver z) throws IOException { //запуск игры
        if (this.type) {
            this.end(z);
        } else {
            this.question(z);
        }
    }

    public void question(Zver z) throws IOException { //если z вопрос
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer;
        System.out.println("Оно " + this.name + "? (+/-)");
        answer = checkAnswer(br.readLine());
        if (answer.equals("+")) {
            if (this.yes.type) {
                this.yes.end(z);
            } else {
                this.yes.question(z);
            }
        } else {
            if (this.no.type) {
                this.no.end(z);
            } else {
                this.no.question(z);
            }
        }
    }

    public void end(Zver z) throws IOException { //если z животное
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer;
        System.out.println("Это " + this.name + "? (+/-)");
        answer = checkAnswer(br.readLine());
        if (answer.equals("+")) {
            System.out.println("Угадала!");
        } else {
            if ((this.parent.no.type) && !(this == this.parent.no)) {
                this.parent.no.end(z);
            } else {
                System.out.println("Не угадала...");
                this.add(); //метод добавления нового животного
            }
        }
        System.out.println("Сыграем еще? (+/-)");
        answer = checkAnswer(br.readLine());
        if (answer.equals("-")) {
            System.out.println("Спасибо за игру!");
        } else {
            z.game(z);
        }
    }

    public static String checkAnswer(String s) throws IOException { // проверка ввода
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Boolean b = true;
        while (b) {
            if (s.equals("+") || s.equals("-")) {
                return s;
            } else {
                System.out.println("Подходит только '+' или '-'. Повторите ввод пожалуйста.");
                s = br.readLine();
            }
        }
        return s;
    }

    public void add() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer;
        System.out.println("что делает этот зверь, чего не может " + this.name + "?");
        answer = br.readLine();
        if (this.parent.type) { // зверь был альтернативой другого зверя
            if (this.parent.parent.yes == this.parent) {
                Zver z1 = this.parent;
                Zver q = new Zver(answer, this.parent.parent, false); //новый вопрос
                this.parent.parent.yes = q;
                System.out.println("Как называется этот зверь?");
                answer = br.readLine();
                Zver z = new Zver(answer, this.parent.parent.yes.yes, true);
                this.parent.parent.yes.no = this;
                z1.parent = this;
                this.no = z1;
            } else {
                Zver z1 = this.parent;
                Zver q = new Zver(answer, this.parent.parent, false); //новый вопрос
                this.parent.parent.no = q;
                System.out.println("Как называется этот зверь?");
                answer = br.readLine();
                Zver z = new Zver(answer, this.parent.parent.no.yes, true);
                this.parent.parent.no.no = this;
                z1.parent = this;
                this.no = z1;
            }
        } else {
            if (this.equals(this.parent.no)) { // зверь был ответом "нет" на вопрос
                Zver q = new Zver(answer, this.parent, false);
                System.out.println("Как называется этот зверь?");
                answer = br.readLine();
                Zver z = new Zver(answer, q, true);
                this.parent.no = q;
                this.parent = q;
                q.yes = z;
                q.no = this;
            } else {
                if (this.equals(this.parent.yes)) { // зверь был ответом "да" на вопрос
                    Zver q = new Zver(answer, this.parent, false);
                    System.out.println("Как называется этот зверь?");
                    answer = br.readLine();
                    Zver z = new Zver(answer, q, true);
                    this.parent.yes = q;
                    this.parent = q;
                    q.yes = z;
                    q.no = this;
                }
            }

        }
    }
}

