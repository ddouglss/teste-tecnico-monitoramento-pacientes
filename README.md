
<header>
    <h1>HealthGo - Backend API de Sinais Vitais</h1>
</header>

<section>
    <h2>📂 Estrutura do Projeto</h2>
    <ul>
        <li><strong>controller</strong>: Endpoints REST (<code>SinalVitalController</code>)</li>
        <li><strong>service</strong>: Regras de negócio (<code>SinalVitalService</code>)</li>
        <li><strong>entity</strong>: Mapeamento JPA (<code>SinalVital</code>)</li>
        <li><strong>repository</strong>: Interfaces JPA Repository (<code>SinalVitalRepository</code>)</li>
        <li><strong>dto</strong> (opcional, branch <code>feature/dto</code>): Transferência de dados via DTOs</li>
        <li><strong>configuration</strong>: Configurações gerais do Spring (ex.: CORS, segurança)</li>
    </ul>
</section>

<section>
    <h2>🛠 Tecnologias Utilizadas</h2>
    <ul>
        <li>Java 17+</li>
        <li>Spring Boot 3.x</li>
        <li>Spring Data JPA</li>
        <li>H2 ou outro banco relacional</li>
        <li>Lombok</li>
        <li>Docker / Docker Compose</li>
        <li>Maven</li>
    </ul>
</section>

<section>
    <h2>🚀 Como Rodar a Aplicação</h2>

    <h3>1. Clonar o repositório</h3>
    <pre><code>git clone https://github.com/seu-usuario/healthgo-backend.git
cd healthgo-backend</code></pre>

    <h3>2. Rodar via Maven</h3>
    <pre><code>./mvnw clean install
./mvnw spring-boot:run</code></pre>
    <p>A aplicação estará disponível em: <code>http://localhost:8080</code></p>

    <h3>3. Rodar via Docker</h3>
    <h4>a) Build da imagem Docker</h4>
    <pre><code>docker build -t healthgo-backend .</code></pre>

    <h4>b) Rodar container</h4>
    <pre><code>docker run -p 8080:8080 healthgo-backend</code></pre>

    <h4>c) Usando docker-compose</h4>
    <pre><code>docker-compose up --build</code></pre>
</section>

<section>
    <h2>⚡ Endpoints Principais</h2>
    <table>
        <thead>
            <tr>
                <th>Método</th>
                <th>Endpoint</th>
                <th>Descrição</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>POST</td>
                <td>/api/sinais-vitais/upload</td>
                <td>Upload CSV de sinais vitais. Recebe <code>file</code> e opcional <code>dataFormat</code>.</td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/sinais-vitais/{pacienteId}</td>
                <td>Lista todos os sinais vitais de um paciente.</td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/sinais-vitais/{pacienteId}/intervalo?startTimestamp=HH:mm:ss&endTimestamp=HH:mm:ss</td>
                <td>Lista sinais vitais de um paciente em intervalo de tempo.</td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/sinais-vitais/{pacienteId}/download</td>
                <td>Download completo de sinais vitais do paciente em CSV.</td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/sinais-vitais/{pacienteId}/download-intervalo?startTimestamp=HH:mm:ss&endTimestamp=HH:mm:ss</td>
                <td>Download filtrado por intervalo em CSV.</td>
            </tr>
        </tbody>
    </table>
    <p>O <code>dataFormat</code> opcional permite personalizar o formato de hora do CSV (padrão <code>HH:mm:ss.SS</code>).</p>
</section>

<section>
    <h2>🧪 Testando a API</h2>
    <p>Você pode testar usando <strong>Postman</strong>, <strong>Insomnia</strong> ou frontend.</p>
    <p>Exemplo de upload via curl:</p>
    <pre><code>curl -X POST "http://localhost:8080/api/sinais-vitais/upload" \
  -F "file=@sinais.csv" \
  -F "dataFormat=HH:mm:ss"</code></pre>
</section>

<section>
    <h2>📝 Observações</h2>
    <ul>
        <li>Na branch <code>feature/dto</code> explorei o uso de DTOs e camadas extras para arquitetura, não obrigatório no desafio.</li>
        <li>Configure corretamente o banco em <code>application.properties</code> ou <code>application.yml</code>.</li>
        <li>CORS configurado para frontend em <code>http://localhost:5173</code>.</li>
    </ul>
</section>

<section>
    <h2>📸 Demonstração</h2>
    <p><img width="1904" height="946" alt="image" src="https://github.com/user-attachments/assets/bb991a18-2808-4b0b-b7cd-02df50852891" />
<a href= "https://drive.google.com/file/d/1jtcDO9olWyZAZYvQzr9H9b-qkVtzD3P9/view?usp=sharing"> Link do video no driver</a>
</section>

<section>
    <h2>📌 Contato</h2>
    <p>Douglas Souza <br>
    GitHub: <a href="https://github.com/ddouglss" target="_blank">https://github.com/ddouglss</a></p>
</section>

</body>
</html>
