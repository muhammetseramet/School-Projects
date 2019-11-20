from PyPDF2 import PdfFileReader
from stop_words import get_stop_words
from bs4 import BeautifulSoup
import csv
import requests
import urllib.request
import collections
import pandas as pd
import matplotlib.pyplot as plt
from wordcloud import WordCloud
from nltk.corpus import stopwords
import sklearn.feature_extraction.text as Tfidf
import math

# bunlar tf için.. tf bunlarda tutulacak
hepsi = []
hepsi_dict = []

# bunlar da gidip tf idf leri tutacak..
tf_dict = {}
tfidf_dict = {}
all_docs = {}
numberOfDocs = 0

# bu gitcek verilen urlden indirme yapcak..
def download_pdf(url):
    filename = url.split('/')[-1]
    urllib.request.urlretrieve(url, filename)
    print('\n>> Download of the file named "' + filename + '" is now completed...\n')
    get_pdf(filename)

# bu bizim belirleğimiz siteye gidicek..
# ordan bütün indirilebilir linkleri bulup
# inidrme işlemini yapmak üzere download_pdf func. yollayacak...
def get_all_pdf():
    #https://muratcanganiz.com/
    #    archive_url = "http://mimoza.marmara.edu.tr/~omer.korcak/"

    archive_url = "http://mimoza.marmara.edu.tr/~omer.korcak/"
    r = requests.get(archive_url)

    soup = BeautifulSoup(r.content, features="html.parser")

    links = soup.find_all('a')
	# bu sonu pdf ile biten linkleri bulucak.
    pdfLinks = [archive_url + link['href'] for link in links if link['href'].endswith('pdf')]

    for link in pdfLinks:
        print('\n>> Found a new pdf file at ' + link + ' location...\n')
        download_pdf(link)
        global numberOfDocs
        numberOfDocs += 1

# pdf indikten hemen sonra buraya gelecek..
# burada pdf okuyacak ve kelimeleri tutacak..
def get_pdf(file):
    pdf = PdfFileReader(file, strict=False)
    stop = set(stopwords.words('english'))
    stop_words = get_stop_words('english')
    stop_words.append(stop)
    stop_words.extend(["=", ",", "-"])
    num = pdf.getNumPages() # kaç sayfa var dökümanda
    filtered = []
    for i in range(num):
        page = pdf.getPage(i)
        text = page.extractText() # sayfayı stringe çevir..

        wordList = text.split() # string boşluklara göre split et ve
                                # list yapısında tut..
        for word in wordList:
            word = word.lower()
            if word not in stop_words:
                hepsi.append(word)
                filtered.append(word)
                # number varsa sil..
                filtered = [x for x in filtered if not any(x1.isdigit() for x1 in x)]

    hepsi_dict.append(filtered)
    # burada term frequency hesaplamamız lazım..
    # her sayfayı alıp vectorlemesi lazım..
    vectorizer = Tfidf.CountVectorizer(input='content', binary=False,
                                       ngram_range=(1, 1), stop_words="english")
    tf_vec_fit = vectorizer.fit_transform(filtered)
    tf_vector = tf_vec_fit.toarray().sum(axis=0).tolist()
    feature_names = vectorizer.get_feature_names()
    # burdada kelimeleri ve bunların tf lerini tf_dict tutcak..
    for i in range(len(feature_names)):
        if feature_names[i] not in tf_dict.keys():
            tf_dict[feature_names[i]] = tf_vector[i]
        else:
            tf_dict[feature_names[i]] += tf_vector[i]
    all_docs[file] = list(set(filtered))

# bu bulduğumuz tf kelimelerinin hepsini csv file yazacak..
def tf_list():
    cnt = collections.Counter()

    for word in hepsi:
        cnt[word] += 1

    # dosyayı oluşur ve yaz..
    with open('tf_list.csv', 'w', newline='', encoding="utf-8") as csvfile:
        fieldNames = ['word', 'count']
        writer = csv.writer(csvfile)
        writer.writerow(fieldNames)
        # en çok kullanılan 50 kelimeyi yazdır.
        for key, value in cnt.most_common(50):
            writer.writerow([key] + [value])

    print('\n>> Tf List is ready as a csv file...\n')

# bu gidip tf için word cloud oluşturacak...
def generate_word_cloud(name):
    df = pd.read_csv(name, encoding="utf-8")
    comment_words = ' '
    for val in df.values:
        val = str(val)

        tokens = val.split()
        for word in tokens:
            comment_words = comment_words + word + ' '

    wordcloud = WordCloud(background_color='White', relative_scaling=0.7, width=1366,
                          height=768, repeat=False).generate(comment_words)

    plt.imshow(wordcloud)
    plt.axis("off")
    print('\n>> Tf List is now saved as a word-cloud file...\n')
    plt.savefig("tf_list.pdf", format='pdf')

# bu gidip tf idf hesaplamalarını yapacak..
def get_tf_idf():
    for word in list(tf_dict.keys()):
        word_counter = 0
        # kaç tane döküman aynı kelimeyi içeriyor diye bak..
        for doc in list(all_docs.values()):
            if word in doc:
                word_counter += 1
        if word_counter != 0:
            tfidf = tf_dict[word] * math.log(numberOfDocs / word_counter)
            tfidf_dict[word] = tfidf

    # bütün tfidfleri tutması için gerekli dataframe
    tfidf_data = {'Words': list(tfidf_dict.keys()),
                  'TfIdf': list(tfidf_dict.values())}
    df = pd.DataFrame(tfidf_data, columns=['Words', 'TfIdf'])
    df = df.sort_values('TfIdf', ascending=False)
    df[:50].to_csv('tfidf_list.csv', encoding='utf-8', mode='w', index=False, header=False)
    print('\n>> Tf-Idf List is ready as a csv file...\n')

# bu gidip tf idf world cloudu oluşturcak..
def get_tf_idf_world_cloud():
    tfidf_first_50_dict = {}
    with open('tfidf_list.csv', newline='\n') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            tfidf_first_50_dict[row[0]] = float(row[1])
    csvfile.close()

    # Word cloud for tf idf
    tfidf_cloud = WordCloud(background_color='White', relative_scaling=0.7, width=1366,
                            height=768).generate_from_frequencies(tfidf_first_50_dict)
    plt.imshow(tfidf_cloud)
    plt.axis('off')
    plt.savefig('tfidf_wordCloud.pdf', format='pdf')
    print('\n>> Tf-Idf List is now saved as a word-cloud file...\n')


if __name__ == "__main__":
    get_all_pdf()
    tf_list()
    generate_word_cloud('tf_list.csv')
    get_tf_idf()
    get_tf_idf_world_cloud()
