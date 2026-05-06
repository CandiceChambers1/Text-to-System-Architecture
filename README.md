# Text to System Architecture

> Automatically generate SysML and AADL system architecture models from natural language specifications.

---

## Table of Contents

- [Overview](#overview)
- [How It Works](#how-it-works)
- [Repository Structure](#repository-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Step 1 — NLP Preprocessing (Python)](#step-1--nlp-preprocessing-python)
  - [Step 2 — Model Generation (Java)](#step-2--model-generation-java)
- [Use Cases](#use-cases)
- [Output Formats](#output-formats)
- [Grammar — SADL](#grammar--sadl)
- [Contact](#contact)

---

## Overview

This project presents a novel approach to automatically generating system design models from natural language specifications. It addresses the challenges of ambiguity and complexity in manual model creation by introducing a formal grammar called **SADL** (Structural Architectural Design Language).

SADL is an extension of a predefined grammar based on laws of physics that facilitates the translation of natural-language text into architectural representations of structural specifications. It serves as an intermediate representation that is then transformed into **SysML** or **AADL** modeling languages.

This approach is demonstrated through five use cases across the aerospace and electromechanical domains, showcasing the grammar's flexibility and effectiveness in capturing salient system features and automatically generating accurate models. This work contributes to the **Model-based Systems Engineering (MBSE)** field by providing a systematic, efficient method for automatically translating natural-language specifications into formal systems models.

---

## How It Works

The pipeline consists of two stages:

```
Natural Language Text
        │
        ▼
┌───────────────────┐
│  Python (NLTK)    │  ← Stopword removal, synonym normalization,
│  NLP Preprocessor │    noun formatting → produces SADL-formatted text
└───────────────────┘
        │  *_NLP.txt
        ▼
┌───────────────────┐
│  Java (ANTLR4)    │  ← Parses SADL grammar, visits parse tree,
│  Model Generator  │    generates SysML (.eapx) and AADL (.aadl)
└───────────────────┘
        │
        ▼
  SysML / AADL Models
```

1. **NLP Preprocessing** (`Python/src/main/NewFormatter.py`): Reads a plain-text system description, removes irrelevant stopwords while preserving domain-specific keywords (functional verbs, energy types, physical states), normalizes synonyms to a controlled vocabulary, and formats nouns to the `UPPER_SNAKE_CASE` convention required by the SADL grammar. The result is a clean `*_NLP.txt` file.

2. **Model Generation** (`Java/src/main/Main.java`): Feeds the `*_NLP.txt` file into an ANTLR4-generated parser built on the SADL grammar (`Sysml.g4`). The visitor traverses the parse tree and drives `CreateSysML` and `CreateAADL` to emit the final architecture models.

---

## Repository Structure

```
Text-to-System-Architecture/
├── Python/
│   └── src/
│       ├── data/               # Raw natural-language input files (*_Manual.txt)
│       └── main/
│           ├── NewFormatter.py # NLP preprocessing script
│           └── NewFormatter.ipynb
│
└── Java/
    └── src/
        ├── data/               # SADL-formatted NLP output files (*_NLP.txt)
        ├── lib/                # ANTLR4 runtime + generated lexer/parser (Sysml.g4)
        ├── aadl/               # Reference AADL files
        ├── main/               # Java source: visitor, model generators, data classes
        └── gen/
            ├── sysml/          # Generated SysML artefacts (models, diagrams, XML)
            └── aadl/           # Generated AADL artefacts (models, diagrams)
```

---

## Prerequisites

| Tool | Version | Purpose |
|------|---------|---------|
| Python | 3.7+ | NLP preprocessing |
| `nltk` | latest | Tokenisation, stopword removal, WordNet synonyms |
| Java | 11+ | Model generation |
| ANTLR4 | 4.9.2 | Grammar parsing (JAR included in `Java/src/lib/`) |
| Enterprise Architect *(optional)* | any | Opening generated `.eapx` SysML models |

Install Python dependencies:

```bash
pip install nltk
```

NLTK corpora are downloaded automatically the first time `NewFormatter.py` runs (`wordnet`, `stopwords`, `punkt`, `averaged_perceptron_tagger`).

---

## Getting Started

### Step 1 — NLP Preprocessing (Python)

1. Place your plain-text system description in `Python/src/data/` following the naming convention `<SystemName>_Manual.txt`.

2. Open `Python/src/main/NewFormatter.py` and update the `input_paragraph` path near the bottom of the file to point to your new input file.

3. Run the script:

```bash
cd Python/src/main
python NewFormatter.py
```

The formatted output is written to `Python/src/gen/<SystemName>_NLP.txt` (copy this file to `Java/src/data/` before the next step).

### Step 2 — Model Generation (Java)

1. Ensure the `*_NLP.txt` file produced above is present in `Java/src/data/`.

2. Open `Java/src/main/Main.java` and set `inputFileName` to your file:

```java
String inputFileName = "Java/src/data/<SystemName>_NLP.txt";
```

3. Compile and run from the repository root (the ANTLR4 JAR is already included):

```bash
javac -cp "Java/src/lib/antlr-4.9.2-complete.jar" \
      -d out \
      Java/src/main/*.java Java/src/lib/Sysml*.java

java -cp "out:Java/src/lib/antlr-4.9.2-complete.jar" main.Main
```

Generated models are written to `Java/src/gen/sysml/` and `Java/src/gen/aadl/`.

---

## Use Cases

Five system descriptions are included as worked examples:

| System | Domain | Input File |
|--------|--------|------------|
| Active/Standby System (ASS) | Aerospace / Fault-tolerant computing | `ActiveStandby_Manual.txt` |
| Coffee Maker | Electromechanical | `Coffeemaker_Manual.txt` |
| Flight Guidance System (FGS) | Aerospace avionics | `FGS_Manual.txt` |
| Hair Dryer | Electromechanical | `HairDryer_Manual.txt` |
| Vacuum Cleaner | Electromechanical | `VaccuumCleaner_Manual.txt` |

---

## Output Formats

| Format | Description | Location |
|--------|-------------|----------|
| `.eapx` | Enterprise Architect SysML project (BDD / IBD diagrams) | `Java/src/gen/sysml/models/` |
| `.xml` | XML representation of the SysML model | `Java/src/gen/sysml/xml/` |
| `.aadl` | AADL architecture description | `Java/src/gen/aadl/models/` |

---

## Grammar — SADL

The SADL grammar (`Java/src/lib/Sysml.g4`) captures two classes of statements:

- **Structural statements** — component composition (`consists`), port declarations (`port_components`, `internal_components`), connections (`connected to`), and instantiations (`instantiates`).
- **Functional statements** — energy/material flows using verbs from the *Functional Basis* vocabulary (e.g., `imports`, `exports`, `transfers`, `converts`, `stores`, `regulates`, …).

Nouns are written in `UPPER_SNAKE_CASE`; adjective qualifiers follow standard title-case (e.g., `Electrical energy`, `Hot gas`).

---

## Contact

For questions, suggestions, or collaborations:

- **Candice Chambers** — chambersc2017@my.fit.edu
- **Parth Ganeriwala** — pganeriwala2022@my.fit.edu
- **Summer Mueller** — smueller2023@my.fit.edu
- **Siddartha Bhattacharyya** — sbhattacharyya@fit.edu
