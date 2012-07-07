var Person = function (name) {
    if (this instanceof Person === false) {
        return new Person(name);
    }
    this.name = name;
    if (typeof Person.prototype.getName === "undefined") {
        Person.prototype.getName = function () {
            return this.name;
        };
    }
    if (typeof Person.prototype.say === "undefined") {
        Person.prototype.say = function () {
            return "this is " + this.name;
        };
    }
};

function numberTest (arg1, arg2) {
    if (typeof arg1 !== "number" || typeof arg2 !== "number") {
        return 0;
    } else {
        return arg1 + arg2;
    }
}

function stringTest (arg) {
    return "hello " + arg;
}

function jsonTest () {
    return {name : "name", age : 1};
}
