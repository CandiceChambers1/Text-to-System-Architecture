#!/usr/bin/env python
# coding: utf-8

# # Text Formatter

# ### Imports

# In[1]:


import nltk
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.corpus import wordnet
from nltk.stem.snowball import SnowballStemmer
import os

# ## Downloads

# In[2]:


nltk.download('wordnet')
nltk.download('stopwords')
nltk.download('punkt')
nltk.download('averaged_perceptron_tagger')


# ## Preprocessing Functions

# ### Stopwords

# In[3]:


stop_words = set(stopwords.words("english"))

# Words to be preserved for understanding basis in Grammar
stopwords_remove = ['from','to','and', 'it', 'with']


# ### List of Words and Stopwords

# In[4]:


energy_list = ['Human', 'Acoustic', 'Biologcal', 'Chemical', 'Electrical', 'ElectroMagnetic', 'Hydraulic', 'Magnetic',
               'Mechanical', 'Thermal', 'Radioactive', 'Pneumatic']

# List of words present in Functional basis
separate_list = ['separate', 'divide', 'extract', 'remove', 'isolate', 'sever', 'disjoin', 'detach', 'detaches',
                 'release', 'sort', 'split', 'disconnect', 'subtract', 'refine', 'filter', 'purify', 'purifies', 'percolate', 'strain', 'clear', 'cut', 'drill', 'lathe', 'polish',
                 'polishes', 'sand']

distribute_list = ['distribute', 'diffuse', 'dispel', 'disperse', 'dissipate', 'diverge', 'scatter']

import_list = ['import', 'form entrance', 'allow', 'allows','input', 'capture']

export_list = ['export', 'dispose', 'eject', 'emit', 'empty', 'remove', 'destroy', 'eliminate']

transfer_list = ['transfer', 'transport', 'transmit', 'carry', 'carries', 'deliver', 'advance', 'lift', 'move',
                 'conduct', 'convey']
guide_list = ['guide', 'translate', 'rotate','direct', 'shift', 'steer', 'straighten', 'switch', 'switches',
              'move', 'relocate', 'spin', 'turn', 'constrain',
              'unfasten', 'unlock']
couple_list = ['couple', 'join', 'link', 'associate', 'assemble', 'fasten', 'attach', 'attaches']


mix_list = ['mix', 'mixes', 'add', 'blend', 'coalesce', 'combine', 'pack']

actuate_list = ['actuate', 'enable', 'initiate', 'start', 'turn on']

regulate_list = ['regulate', 'increase', 'decrease', 'control', 'equalize', 'limit', 'maintain', 'open', 'close',
                 'delay', 'interrupt']
change_list = ['change', 'increment', 'decrement', 'shape', 'condition', 'adjust', 'modulate', 'demodulate', 'invert',
               'normalize', 'rectify',
               'rectifies', 'reset', 'scale', 'vary', 'varies', 'modify', 'modifies', 'amplify', 'amplifies', 'enhance',
               'magnify', 'magnifies',
               'multiply', 'multiplies', 'attenuate', 'dampen', 'reduce', 'compact', 'compress', 'compresses', 'crush',
               'crushes', 'pierce', 'deform', 'form', 'prepare', 'adapt', 'treat']

stop_list = ['stop', 'prevent', 'inhibit', 'end', 'hault', 'pause', 'interrupt', 'restrain', 'disable', 'turn off',
             'shield', 'insulate', 'protect', 'resist']

convert_list = ['convert', 'condense', 'create', 'decode', 'differentiate', 'digitize', 'encode', 'evaporate',
                'generate', 'integrate', 'liquefy', 'liquifies', 'process', 'solidify',
                'solidifies', 'transform']
store_list = ['store', 'contain', 'collect', 'accumulate', 'enclose', 'absorb', 'consume', 'fill', 'reserve']

supply_list = ['supply', 'supplies', 'provide', 'replenish', 'replenishes', 'retrieve']

sense_list = ['sense', 'detect', 'measure', 'feel', 'determine', 'discern', 'perceive', 'recognize', 'identify',
              'identifies', 'locate']
indicate_list = ['indicate', 'track', 'display', 'announce', 'show', 'denote', 'record', 'register', 'mark', 'time',
                 'expose', 'select']
process_list = ['process', 'processes', 'calculate', 'check']

energize_list = ['energize', 'deenergize']

# consist_list = ['consist', 'include']

# Different adjectives to be used as a Fuzzy system
temperature = ['Cryogenic', 'Frozen', 'Chilled', 'Freezing', 'Cold', 'Cool', 'Normal Temperature', 'Room Temperature',
               'Lukewarm', 'Toasty', 'Mild', 'Warm',
               'Heated', 'Hot', 'Cooked', 'Toasted', 'Boiling', 'Burning', 'Steaming']
velocity = ['Static', 'Still', 'Creeping', 'Sluggish', 'Slow', 'Flowing', 'Moving', 'Fast', 'Rapid']
material = ['Solid', 'Liquid', 'Gas', 'Mixture']

#Specific Keywords for BBD and IBD generation
internal_component = ['internal_components', 'internally','internal']
port_component = ['port_component','port', 'ports']



# In[5]:


functional_verbs = []
all_lists = [separate_list, distribute_list, import_list, export_list, 
            transfer_list, guide_list, couple_list, mix_list, actuate_list, 
            regulate_list, change_list, stop_list, convert_list, store_list, 
            supply_list, sense_list, indicate_list, process_list, 
            energize_list, port_component]

allPreserveList = [temperature, functional_verbs, velocity, stopwords_remove, 
                  energy_list, material]


# ### Adding Characters in Front of the Reserved Words

# In[6]:


def addCharacters(List):
    new_list = []
    for elem in List:
        if elem[-1] == "h":
            new_list.append(elem + 'es')
        elif elem[-1] == 'y':
            if elem[-2] in ['a', 'e', 'i', 'o', 'u']:
                new_list.append(elem + 's')
            else:
                new_list.append(elem[:-1] + 'ies')
        elif elem[-1] == 's':
            if elem[-2] == 's':
                new_list.append(elem + 'es')
            else:
                new_list.append(elem + 's')
        elif elem[-1] == 'x':
            new_list.append(elem + 'es')
        else:
            new_list.append(elem + 's')
    List = []
    List.extend(new_list)
    
    return List


# In[7]:


def editLists(all_lists):
    temporary_list = []
    new_all_lists = []
    for i in all_lists: 
        extended_list = addCharacters(i)
        temporary_list = extended_list
        new_all_lists.append(temporary_list)
        temporary_list = []
        for i in extended_list:
            functional_verbs.append(i)
            i = functional_verbs
            
    return new_all_lists


# ### Perserving words present in temperature, velocity, energy and functional verbs

# In[8]:

# Adding some words to the stop words list that we do not require and removing words we need 
stopwords_add = ['The', 'A']
for i in stopwords_add:
    stop_words.add(i)

for i in stopwords_remove:
    stop_words.remove(i)
    
ps = SnowballStemmer("english")


# ## Preprocessing Text

# In[9]:


def remove_stopwords_from_raw_text(input_paragraph):
    try:
        words = nltk.word_tokenize(input_paragraph)
        return remove_stopwords_from_tokenized_text(words)

    except Exception as e:
        print(str(e))
        
def remove_stopwords_from_tokenized_text(words):
    try:
        output_list = []

        for w in words:
            if w not in stop_words:
                output_list.append(w)

        return output_list

    except Exception as e:
        print(str(e))


# In[10]:


# Consists can be used in the same way as before
# Converting words not in nltk synonym list to the ones that are accepted
# Consists can be used in the same way as before
# Converting words not in nltk synonym list to the ones that are accepted
def changeSynonyms(final_list, new_all_list):
    index = 0
    one_list = []

    # if all(a in final_list for a in ("internal", ":")):
    #     index_internal = final_list.index("internal")
    #     final_list.remove(final_list[index_internal + 1])
    #     final_list[index_internal] = internal_component[0]
    # This assumes that IBD has more than one component
    for i in final_list:
        if i == "internal":
            if final_list[final_list.index(i) + 2] == ":":
                final_list.remove(final_list[final_list.index(i) + 1])
                final_list[final_list.index(i)] = internal_component[0]

    # for i in final_list:
    #     index_internal = final_list.index("internal")
    #     final_list.remove(final_list[index_internal + 1])
    #     final_list[index_internal] = internal_component[0]
    #

    for i in final_list:
        for j in new_all_list:
            one_list = j
            for k in one_list:
                if (i == k):
                    if i.lower()[-1] == 's':
                        if one_list[0][-1] == 'x':
                            final_list[index] = one_list[0] + 'es'
                        elif one_list[0][-1] == 'y':
                            final_list[index] = one_list[0][:-1] + 'ies'
                        else:
                            final_list[index] = one_list[0]
                    break
        index = index + 1
    return final_list


# In[11]:


def createParagraphs(final_list): 
    new_para = ""
    for ele in final_list:
        if ele == ".":
            new_para = new_para + ele
        else:
            new_para = new_para + " " + ele
    return new_para


# In[12]:


def formatNouns(new_para):
    new_para_latest = ""
    for i in new_para.split("."):
        if "consists" in i:
            within_words = i.split()
            for words in within_words:
                if (words == "and" or words == "consists" or words == "," or words == "port_components"
                        or words == "internal_components" or words == ":"):
                    new_para_latest = new_para_latest[:-1] + " " + words + " "
                else:
                    new_para_latest = new_para_latest + words.upper() + "_"
            new_para_latest = new_para_latest[:-1] + ".\n"
        elif "connected" in i:
            within_words = i.split()
            for words in within_words:
                if (words == "connected" or words == "and" or words == ',' or words == "port_components"):
                    new_para_latest = new_para_latest[:-1] + " " + words + " "
                elif (words == "to"):
                    new_para_latest = new_para_latest + " " + words + " "
                else:
                    new_para_latest = new_para_latest + words.upper() + "_"
            new_para_latest = new_para_latest[:-1] + ".\n"
        elif "instantiates" in i:
            within_words = i.split()
            for words in within_words:
                if (words == "instantiates"):
                    new_para_latest = new_para_latest[:-1] + " " + words + " "
                else:
                    new_para_latest = new_para_latest + words.upper() + "_"
            new_para_latest = new_para_latest[:-1] + ".\n"
        elif ("imports" in i) or ("exports" in i) or ("transfers" in i) or ("guides" in i) or ("supplies" in i):
            within_words = i.split()
            index_func = 0
            index_from = 0
            index_to = 0
            for m in range(len(within_words)):
                words = ['imports', 'exports', 'transfers', 'guides', 'supplies']
                for i in words:
                    if i in within_words:
                        occured_word = i
                index_func = within_words.index(occured_word)
                if (within_words[m] == "from"):
                    index_from = m
                elif (within_words[m] == "to"):
                    index_to = m
                else:
                    continue
            m = 0
            while (m < len(within_words)):
                if (index_func > 0 and m == 0):
                    for x in range(index_func):
                        new_para_latest = new_para_latest + within_words[m].upper() + "_"
                        m = m + 1
                    new_para_latest = new_para_latest[:-1]
                elif (index_from > 0 and index_to > 0 and m == index_from):
                    new_para_latest = new_para_latest + " from" + " "
                    m = m + 1
                    for x in range(index_from + 1, index_to):
                        new_para_latest = new_para_latest + within_words[m].upper() + "_"
                        m = m + 1
                    new_para_latest = new_para_latest[:-1] + " to "
                    m = m + 1
                    for x in range(index_to + 1, len(within_words)):
                        new_para_latest = new_para_latest + within_words[m].upper() + "_"
                        m = m + 1
                    new_para_latest = new_para_latest[:-1]

                elif (index_from > 0 and m == index_from):
                    new_para_latest = new_para_latest + " from "
                    m = m + 1
                    for x in range(index_from + 1, len(within_words)):
                        new_para_latest = new_para_latest + within_words[m].upper() + "_"
                        m = m + 1
                    new_para_latest = new_para_latest[:-1]
                elif (index_to > 0 and m == index_to):
                    new_para_latest = new_para_latest + " to "
                    m = m + 1
                    for x in range(index_to + 1, len(within_words)):
                        new_para_latest = new_para_latest + within_words[m].upper() + "_"
                        m = m + 1
                    new_para_latest = new_para_latest[:-1]
                else:
                    new_para_latest = new_para_latest + " " + within_words[m]
                    m += 1
            new_para_latest = new_para_latest + ".\n"

        elif (("converts" in i) or ("mixes" in i) or ("couples" in i) or ("separates" in i) or ("energizes" in i) or (
                "deenergizes" in i) or ("stores" in i) or ("stops" in i) or ("changes" in i) or ("regulates" in i)):
            within_words = i.split()
            words = ['converts', 'mixes', 'couples', 'separates', 'energizes', 'deenergizes', 'stores', 'stops',
                     'changes',
                     'regulates']
            for i in words:
                if i in within_words:
                    occured_word = i
            index_of_ow = within_words.index(occured_word)
            for j in range(len(within_words)):
                if j < index_of_ow:
                    new_para_latest = new_para_latest + within_words[j].upper() + "_"
                elif j == index_of_ow:
                    new_para_latest = new_para_latest[:-1] + " " + within_words[j]
                else:
                    new_para_latest = new_para_latest + " " + within_words[j]
            new_para_latest = new_para_latest + ".\n"
        elif "distributes" in i:
            within_words = i.split()
            index = within_words.index("distributes")
            index_to = within_words.index("to")
            for x in range(len(within_words)):
                if x < index:
                    new_para_latest = new_para_latest + within_words[x].upper() + "_"
                elif x == index:
                    new_para_latest = new_para_latest[:-1] + " " + within_words[x]
                elif x == index_to:
                    new_para_latest = new_para_latest + " " + within_words[x] + " "
                else:
                    if x > index_to:
                        if within_words[x] == "and":
                            new_para_latest = new_para_latest[:-1] + " " + within_words[x] + " "
                        else:
                            new_para_latest = new_para_latest + within_words[x].upper() + "_"
                    else:
                        new_para_latest = new_para_latest + " " + within_words[x]
            new_para_latest = new_para_latest[:-1] + ".\n"
        else:
            continue
    return new_para_latest

# In[13]:


def convert_synonyms(word):
    var = False
    exchange_word = ''
    try:
        stemmed_word = ps.stem(word)
        if ((word == '.') or (word == ',')):
            return word
        new_word = '"' + stemmed_word + '"'
        for syn in wordnet.synsets(new_word):
            for lm in syn.lemmas():
                for x in functional_verbs:
                    if (lm.name()).lower() == x.lower():
                        var = True;
                        exchange_word = x
        if var == True:
            return exchange_word
            var = False
        else:
            return word
    except Exception as e:
        word.upper()


# In[14]:


def convert_if_in_NLTK(new_para_latest):
    new_final_list = []
    for item in word_tokenize(new_para_latest):
        var = False
        for i in energy_list:
            if i.lower() == item.lower():
                new_final_list.append(i)
                var = True
                break
        if var == True:
            continue
        else:
            for j in temperature:
                if j.lower() == item.lower():
                    new_final_list.append(j)
                    var = True
                    break
            if var:
                continue
            else:
                for k in velocity:
                    if k.lower() == item.lower():
                        new_final_list.append(k)
                        var = True
                        break
                if var:
                    continue
                else:
                    for l in material:
                        if l.lower() == item.lower():
                            new_final_list.append(l)
                            var = True
                            break
                    if var:
                        continue
                    else:
                        new_item = convert_synonyms(item)
                        if (new_item == None):
                            new_final_list.append(item)
                        else:
                            new_final_list.append(new_item)

                            
    return new_final_list


# In[15]:


def finalVersion(new_final_list):
    str = ''
    for element in new_final_list:
        if element == '.':
            str = str + element + "\n"
        else:
            str = str + " " + element

    send_to_grammar = ""
    additional_reserved_words = [',', 'consists', 'connected', 'internal_components']
    for i in str.split("."):
        indexes = []
        within_words = i.split()
        for j in within_words:
            if (j.isupper() or (j in functional_verbs) or (j in temperature) or (j in velocity) or (j in material) or (
                    j in additional_reserved_words) or (j in stopwords_remove)):
                continue
            else:
                indexes.append(within_words.index(j))
        if len(indexes) >= 2:
            for j in range(len(indexes) - 1):
                if (indexes[j] + 1 == indexes[j + 1]):
                    within_words[indexes[j]] = within_words[indexes[j]][:1].upper() + within_words[indexes[j]][1:]
        if (i != "\n"):
            for j in within_words:
                send_to_grammar = send_to_grammar + j + " "
            send_to_grammar = send_to_grammar[:-1] + ".\n"

    return send_to_grammar


### Input Text

input_paragraph = open(r".\Text-to-System-Architecture-\Python\src\data\ActiveStandby_Manual.txt", "r")
## Driver Code


# Driver Code
if __name__ == "__main__":
    functional_verbs = []
    final_list = []
    new_para = ''
    new_para_latest = ''
    new_final_list = []
    send_to_grammar = ''
    new_all_list = []

    # Edit all individual list and update the all_lists
    new_all_list = editLists(all_lists)

    # Remove stopwords from raw text
    final_list = remove_stopwords_from_raw_text(input_paragraph.read())

    # Change synonyms to our preferred  words
    changeSynonyms(final_list, new_all_list)

    # Convert the list to a paragraph
    new_para = createParagraphs(final_list)

    # print(new_para)
    # Reformat the nouns for ANTLR standards
    new_para_latest = formatNouns(new_para)

    #
    new_final_list = convert_if_in_NLTK(new_para_latest)
    send_to_grammar = finalVersion(new_final_list)

    file_name = input_paragraph.name
    result = file_name.split("\\")

    file_name = result[-1]
    name = file_name.split("_")
    f_name = name[0]

    output_file = r".\Text-to-System-Architecture-\Python\src\gen\\" + f_name + "_NLP.txt"

    file = open(output_file, "w")

    # file = open("FGS_NLP.txt", "w")
    file.write(send_to_grammar)

    # print(send_to_grammar)

