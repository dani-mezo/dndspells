package main.java

class Main {
    static void main(String... args) {
        String defaultPathBase = new File( "." ).getCanonicalPath()
        File dndSpells = new File(defaultPathBase + '/src/main/resources/dndspells.txt')
        new DndSpells(source: dndSpells).generate()
    }
}
