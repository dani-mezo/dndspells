package main.java

class DndSpells {
    File source

    private Map<String, Map<String, List<String>>> spells = new HashMap<>()

    private static List<String> CARDINALS = [ '1st', '2nd', '3rd', '4th', '5th', '6th', '7th', '8th', '9th' ]
    private static List<String> CLASSES = [ 'bard', 'cleric', 'druid', 'ranger', 'sorcerer', 'wizard', 'warlock' ]
    private static String LINE_SEPARATOR = System.getProperty("line.separator")

    private String currentSpellLevel

    void generate() {
        Objects.requireNonNull(source)
        source.each { it in CARDINALS ? currentSpellLevel = it : mapSpell(it) }
        spells.each(generateSpellListForClass())
    }

    private void mapSpell(String spellRow) {
        if (!spellRow || spellRow.isEmpty()) return
        def (spell, description) = spellRow.contains(': ') ? spellRow.split(':') : [spellRow, ""]
        def (spellName, classes) = spell.split(' \\(')
        String spellWithDescription = (description as String).isEmpty() ? spellName : "${spellName as String}:${description as String}"
        classes.replace(')', '')
                .tokenize(',')
                *.trim()
                .grep { it in CLASSES }
                .each { String spellClass ->
                    Map<String, List<String>> classSpells = spells.computeIfAbsent(spellClass, { k -> new HashMap<String, List<String>>() })
                    ArrayList<String> classLeveledSpells = classSpells.computeIfAbsent(currentSpellLevel, { k -> new ArrayList<String>() })
                    classLeveledSpells.add(spellWithDescription as String)
                }
    }

    private static Closure generateSpellListForClass() {
        return { String dndClass, Map<String, List<String>> spells ->
            File classSpellsFile = new File("${dndClass}-spells.txt")
            classSpellsFile.text = ''
            CARDINALS.each { String spellLevel ->
                if(!spells.get(spellLevel)) return
                classSpellsFile.append(spellLevel)
                classSpellsFile.append(LINE_SEPARATOR)
                spells.get(spellLevel).each {
                    classSpellsFile.append(it)
                    classSpellsFile.append(LINE_SEPARATOR)
                }
                classSpellsFile.append(LINE_SEPARATOR)
            }
        }
    }
}
